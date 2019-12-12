package org.firstinspires.ftc.teamcode._Auto;

/**
 * OpMode to test AutoLib driving of motors by time or encoder counts
 * Created by phanau on 12/14/15.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * Created by phanau on 12/14/15.
 */



@Autonomous(name="AutoFoundationRedLeft", group ="Test")
//@Disabled
public class AutoFoundationRedLeft extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done
    boolean bFirst;                 // true first time loop() is called

    DcMotor mFr, mBr, mFl, mBl;     // four drive motors (front right, back right, front left, back left)
    DcMotor mIo, mUd;               // two arm motors (in-out, up-down) OPTIONAL

    boolean debug = false;           // run in test/debug mode with dummy motors and data logging
    boolean haveEncoders = true;   // robot has Encoder-based motors

    public AutoFoundationRedLeft() {
    }

    public void init() {

        AutoLib.HardwareFactory mf = null;
        if (debug)
            mf = new AutoLib.TestHardwareFactory(this);
        else
            mf = new AutoLib.RealHardwareFactory(this);

        // get the motors: depending on the factory we created above, these may be
        // either dummy motors that just log data or real ones that drive the hardware

        try {
            mFr = mf.getDcMotor("fr");
            mFl = mf.getDcMotor("fl");
            mBr = mf.getDcMotor("br");
            mBl = mf.getDcMotor("bl");
        }
        catch(IllegalArgumentException iax){

        }
        // OPTIONAL arm motors
        try {
            mIo = mf.getDcMotor("io");
            mUd = mf.getDcMotor("ud");
        }
        catch (IllegalArgumentException iax) {
            mIo = mUd = null;
        }

        mFl.setDirection(DcMotor.Direction.REVERSE);
        mBl.setDirection(DcMotor.Direction.REVERSE);

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // === MAIN DRIVE STUFF ===
        // Drives forward
        mSequence.add(new AutoLib.SideToSide(mFr, mBr, mFl, mBl,.5, .5,  .5, .5,  2.3, true));
        // Wait
        mSequence.add(new AutoLib.SideToSide(mFr, mBr, mFl, mBl,0, 0,  0, 0,  1, true));
        // Rotates 90 degrees clockwise
        mSequence.add(new AutoLib.TurnByTimeStep(mFr, mBr, mFl, mBl,-.2, .2,  .5, true));
        // Wait
        mSequence.add(new AutoLib.SideToSide(mFr, mBr, mFl, mBl,0, 0,  0, 0,  1, true));

//        // Raise the arm using encoders while also extending it for 1 second
//        AutoLib.ConcurrentSequence cs1 = new AutoLib.ConcurrentSequence();
//        if (mUd != null && (debug || !haveEncoders))
//            cs1.add(new AutoLib.TimedMotorStep(mUd, 0.75, 1.0, true)); // we don't support encoders yet in debug mode
//        //else
//        //    cs1.add(new AutoLib.EncoderMotorStep(new EncoderMotor(mUd), 0.75, 1000, true));
//        if (mIo != null)
//            cs1.add(new AutoLib.TimedMotorStep(mIo, 0.5, 1.0, true));
//        mSequence.add(cs1);
//
        // Drives back to pull foundation
        mSequence.add(new AutoLib.SideToSide(mFr, mBr, mFl, mBl,-.5, -.5,  -.5, -.5,  2.3, true));


        // start out not-done, first time
        bDone = false;
        bFirst = true;
    }

    public void loop() {
        // reset the timer when we start looping
        if (bFirst) {
            this.resetStartTime();      // OpMode provides a timer
            bFirst = false;
        }

        // until we're done with the root Sequence, perform the current Step(s) each time through the loop
        if (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done

            if (debug)
                telemetry.addData("elapsed time", this.getRuntime());
        }
    }

    public void stop() {
        telemetry.addData("stop() called", "");
    }
}
