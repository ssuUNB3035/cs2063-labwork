package course.labs.graphicslab

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.children
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.sqrt


class BubbleActivity : Activity(), SensorEventListener {
    // The Main view
    private var mFrame: RelativeLayout? = null

    // Bubble image's bitmap
    private var mBitmap: Bitmap? = null

    // Display dimensions
    private var mDisplayWidth = 0
    private var mDisplayHeight = 0

    // Gesture Detector
    private var mGestureDetector: GestureDetector? = null

    // A TextView to hold the player message
    private var mPlayerMessage: TextView? = null

    // TODO
    //  Create member for storing the lastUpdate
    private var mLastUpdate: Long = 0
    
    // TODO
    //  Create members for the gravity and acceleration arrays
    private val mGravity = FloatArray(3)
    private val mAccel = FloatArray(3)
    
    // TODO
    //  Create members for a SensorManager and Sensor
    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Set up user interface
        mFrame = findViewById(R.id.frame)

        // Set up text view
        mPlayerMessage = findViewById(R.id.startMessage)

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ball)

        // TODO
        //  Get a SensorManager, and then use it to get an accelerometer Sensor.
        //  Set the SensorManager and Sensor members that you defined above appropriately.
        //  If the device does not have an accelerometer, display a message indicating so and exit.
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (mSensorManager!= null) {
            mSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            if (mSensor == null) {
                finish()
            }
            mLastUpdate = System.currentTimeMillis()
        }

        // Get the screen dimensions
        val (width, height) = getScreenDimensions(this)

        // Subtract diameter of the ball from width and height
        mDisplayWidth = width - SCALED_BITMAP_SIZE
        mDisplayHeight = height - SCALED_BITMAP_SIZE
    }

    private fun getScreenDimensions(activity: Activity): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val windowInsets: WindowInsets = windowMetrics.windowInsets
            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())

            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom

            val b = windowMetrics.bounds
            Pair(b.width() - insetsWidth, b.height() - insetsHeight)
        } else {
            val size = Point()
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay // deprecated in API 30
            @Suppress("DEPRECATION")
            display?.getSize(size) // deprecated in API 30
            Pair(size.x, size.y)
        }
    }

    override fun onResume() {
        super.onResume()

        // TODO
        //  Register a listener for the accelerometer
        mSensorManager!!.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI)

        setupGestureDetector()
    }

    override fun onSensorChanged(event: SensorEvent) {
        // TODO
        //  Apply a low- and high-pass filter to the raw sensor values
        //  HINT: See https://developer.android.com/guide/topics/sensors/sensors_motion#sensors-motion-accel
        //        for more information

        // TODO
        //  If there is a BubbleView, use its setSpeedAndDirection() method
        //  to set its speed and direction based on the sensor values and the
        //  current setting of mFilter, which will be one of NO_FILTER, LOW_PASS_FILTER, or HIGH_PASS_FILTER.

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val actualTime = System.currentTimeMillis()
            if (actualTime - mLastUpdate > 500) {
                mLastUpdate = actualTime
                val rawX = event.values[0]
                val rawY = event.values[1]
                val rawZ = event.values[2]

                mGravity[0] = lowPass(rawX, mGravity[0])
                mGravity[1] = lowPass(rawY, mGravity[1])
                mGravity[2] = lowPass(rawZ, mGravity[2])

                mAccel[0] = highPass(rawX, mGravity[0])
                mAccel[1] = highPass(rawY, mGravity[1])
                mAccel[2] = highPass(rawZ, mGravity[2])

                runOnUiThread {
                    setBubbleAccel(rawX,rawY)
                }

            }
        }
    }

    private fun setBubbleAccel(rawX:Float,rawY:Float){
        //var count = 0;
        //while (count < mFrame?.childCount!!) {
        mFrame?.children?.forEach {
            //val bubView = mFrame!!.getChildAt(count) as BubbleView
            val bubView = it as BubbleView
            if (mFilter == NO_FILTER) {
                bubView.setSpeedAndDirection(rawX, rawY)
            } else if (mFilter == LOW_PASS_FILTER) {
                bubView.setSpeedAndDirection(mGravity[0], mGravity[1])
            } else if (mFilter == HIGH_PASS_FILTER) {
                bubView.setSpeedAndDirection(mAccel[0], mAccel[1])
            }
        }
    }

    private fun lowPass(current: Float, gravity: Float): Float {
        val mAlpha = 0.8f
        return gravity * mAlpha + current*(1-mAlpha)
    }

    private fun highPass(current: Float, gravity: Float): Float {
        return current - gravity
    }

    // Nothing to do here, just note that onAccuracyChanged must be implemented
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        // For now we will leave this unimplemented.
    }

    // Set up GestureDetector
    private fun setupGestureDetector() {
        mGestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            // If there is a BubbleView, and a single tap intersects it, remove it.
            // If there are no BubbleViews, create a new BubbleView at the tap's location
            // and add it to mFrame.
            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                val x = event.rawX
                val y = event.rawY
                val numBubbles = mFrame!!.childCount
                if (numBubbles != 0) {
                    val bv = mFrame!!.getChildAt(0) as BubbleView
                    if (bv.intersects(x, y)) {
                        bv.stopMovement()
                    }
                } else {
                    val context = mFrame!!.context
                    val bubbleView = BubbleView(context, x, y)
                    mFrame!!.addView(bubbleView)
                    bubbleView.startMovement()
                    setPlayerMessage()
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                // Cycle through the filter options
                mFilter = (mFilter + 1) % 3
                setPlayerMessage()
            }
        })
    }

    // Update the message that is displayed to the player
    fun setPlayerMessage() {
        val message: String = if (mFrame!!.childCount == 0) {
            getString(R.string.start_message)
        } else {
            when (mFilter) {
                NO_FILTER -> {
                    getString(R.string.no_filter_message)
                }
                LOW_PASS_FILTER -> {
                    getString(R.string.lowpass_filter_message)
                }
                else -> {
                    getString(R.string.highpass_filter_message)
                }
            }
        }

        mPlayerMessage!!.text = message
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()

        // TODO
        //  Unregister the sensor event listener
        mSensorManager!!.unregisterListener(this)
    }

    // BubbleView is a View that displays a bubble.
    // This class handles animating, drawing, and popping amongst other actions.
    inner class BubbleView internal constructor(context: Context?, x: Float, y: Float) :
        View(context) {
        private val mPainter = Paint()
        private var mMoverFuture: ScheduledFuture<*>? = null
        private val mScaledBitmap: Bitmap = Bitmap.createScaledBitmap(mBitmap!!, Companion.SCALED_BITMAP_SIZE,
            Companion.SCALED_BITMAP_SIZE, true)

        // location and direction of the bubble
        private var mXPos: Float
        private var mYPos: Float
        private val mRadius: Float = Companion.SCALED_BITMAP_SIZE / 2.0f

        // Speed of bubble
        private var mDx: Float
        private var mDy: Float

        // Rotation and speed of rotation of the bubble
        private var mRotate: Long = 0
        private val mDRotate: Long

        // setSpeedAndDirection called by onSensorChanged(), values based on
        // accelerometer data and scaled
        fun setSpeedAndDirection(x: Float, y: Float) {
            // NOTE the layout is set to landscape.  This means that the coordinate
            // system does not change, however, the range for x and y are essentially reversed
            mDx = y
            mDy = x

            // TODO
            //  Once your app is working, experiment with alternative
            //  ways of calculating mDx and mDy based on x and y to change
            //  the "feel". Some ideas are shown below.

            // Example 1: Uncomment this to make the ball go faster!
            mDx = 2 * y;
            mDy = 2 * x;

            // Example 2: Uncomment this to make the ball accelerate based on sensor
            // input. You can also scale the contribution of x and y.
            //mDx += y
            //mDy += x
        }

        // Start moving the BubbleView & updating the display
        fun startMovement() {
            // Creates a WorkerThread
            val executor = Executors.newScheduledThreadPool(1)

            // Execute the run() in Worker Thread every REFRESH_RATE
            // milliseconds
            // Save reference to this job in mMoverFuture
            mMoverFuture = executor.scheduleWithFixedDelay({
                doMove()
                this@BubbleView.postInvalidate()
            }, 0, Companion.REFRESH_RATE.toLong(), TimeUnit.MILLISECONDS)
        }

        fun stopMovement() {
            if (null != mMoverFuture) {
                if (!mMoverFuture!!.isDone) {
                    mMoverFuture!!.cancel(true)
                }

                // This work will be performed on the UI Thread
                mFrame!!.post {
                    mFrame!!.removeView(this@BubbleView)
                    setPlayerMessage()
                }
            }
        }

        // Return true if x and y intersect the position of the Bubble
        @Synchronized
        fun intersects(x: Float, y: Float): Boolean {
            val centerX = mXPos + mRadius
            val centerY = mYPos + mRadius
            return sqrt((centerX - x).toDouble().pow(2.0) + (centerY - y).toDouble().pow(2.0)) <= mRadius
        }

        // Draw the Bubble at its current location
        @Synchronized
        override fun onDraw(canvas: Canvas) {
            canvas.save()
            mRotate += mDRotate
            canvas.rotate(mRotate.toFloat(), mXPos + mRadius, mYPos + mRadius)
            canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter)
            canvas.restore()
        }

        // Move the Bubble
        @Synchronized
        private fun doMove() {
            // Don't let the bubble go beyond the edge of the screen
            // Set the speed to 0 if the bubble hits an edge.
            mXPos += mDx
            if (mXPos >= mDisplayWidth) {
                mXPos = mDisplayWidth.toFloat()
                mDx = 0f
            } else if (mXPos <= 0) {
                mXPos = 0f
                mDx = 0f
            }

            mYPos += mDy
            if (mYPos >= mDisplayHeight) {
                mYPos = mDisplayHeight.toFloat()
                mDy = 0f
            } else if (mYPos <= 0) {
                mYPos = 0f
                mDy = 0f
            }
        }

        init {
            // Adjust position to center the bubble under user's finger
            mXPos = x - mRadius
            mYPos = y - mRadius

            // Set speed to 0 initially; it will be updated soon based on sensor input
            mDx = 0f
            mDy = 0f

            // Set speed of rotation to 5
            mDRotate = 5
            mPainter.isAntiAlias = true
        }
    }

    companion object {
        private const val TAG = "BubbleActivity"
        private const val NO_FILTER = 0
        private const val LOW_PASS_FILTER = 1
        private const val HIGH_PASS_FILTER = 2
        private var mFilter = NO_FILTER

        private const val BITMAP_SIZE = 64
        private const val REFRESH_RATE = 5
        private const val SCALED_BITMAP_SIZE = BITMAP_SIZE * 2
    }
}