package org.firstinspires.ftc.teamcode._Auto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode._Libs.AutoLib;

@Autonomous(name="AutoPush", group ="Test")
public class AutoPush extends OpMode
{
    AutoLib.Sequence mSequence;
    DcMotor mFr, mBr, mFl, mBl;
    DcMotor Front;
    DcMotor Back;
    DcMotor Left;
    DcMotor Right;
    boolean bFirst;
    boolean bDone;
    boolean debug = true;



    public void init()
    {
        AutoLib.HardwareFactory mf = null;
        mFr = mf.getDcMotor("fr");
        mFl = mf.getDcMotor("fl");
        mBr = mf.getDcMotor("br");
        mBl = mf.getDcMotor("bl");
        Front = mf.getDcMotor("f");
        Back = mf.getDcMotor("b");
        Left = mf.getDcMotor("l");
        Right = mf.getDcMotor("r");

        mSequence = new AutoLib.LinearSequence();
        mSequence.add(new AutoLib.MoveByTimeStep(mFr, mBr, mFl, mBl, 0.5, 2.0,false));
    }
    public void moveForward(DcMotor front, DcMotor right, DcMotor left, DcMotor back, double power, double seconds)
    {
        AutoLib.TimedMotorStep Step1 = new AutoLib.TimedMotorStep(right,power,seconds,false);
        AutoLib.TimedMotorStep Step2 = new AutoLib.TimedMotorStep(left,-1*power,seconds,false);
    }
    public void moveBackWard(DcMotor front, DcMotor right, DcMotor left, DcMotor back, double power, double seconds)
    {
        AutoLib.TimedMotorStep Step3 = new AutoLib.TimedMotorStep(right,-1*power,seconds, false);
        AutoLib.TimedMotorStep Step4 = new AutoLib.TimedMotorStep(left,power,seconds,false);
    }

    public void moveLeft(DcMotor front, DcMotor right, DcMotor left, DcMotor back, double power, double seconds)
    {
        AutoLib.TimedMotorStep Step5 = new AutoLib.TimedMotorStep(front,power,seconds,false);
        AutoLib.TimedMotorStep Step6 = new AutoLib.TimedMotorStep(back,-1*power,seconds,false);
    }
    public void moveRight(DcMotor front, DcMotor right, DcMotor left, DcMotor back, double power, double seconds)
    {
        AutoLib.TimedMotorStep Step7 = new AutoLib.TimedMotorStep(front,-1*power,seconds,false);
        AutoLib.TimedMotorStep Step8 = new AutoLib.TimedMotorStep(back,power,seconds,false);
    }
    public void loop()
    {
        if (bFirst)
        {
            this.resetStartTime();
            bFirst = false;
        }
        if (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done

            if (debug)
                telemetry.addData("elapsed time", this.getRuntime());
        }
    }
    public void stop()
    {
        telemetry.addData("stop() called", "");
    }

}
