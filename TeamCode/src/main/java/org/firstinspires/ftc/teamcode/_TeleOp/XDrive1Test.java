/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Gonna push this file.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode._TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import java.math.*;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
@TeleOp(name="XDrive1Test", group="Test")  // @Autonomous(...) is the other common choice
//@Disabled
public class XDrive1Test extends OpMode {

	DcMotor motorFrontRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackRight;
	DcMotor motorBackLeft;
	//DcMotor motorArm;
	Servo servoGrabber;


	boolean bDebugFR = false;
	boolean bDebugFL = false;
	boolean bDebugBR = false;
	boolean bDebugBL = false;
	boolean bDebugArm = false;
	boolean bDebugGrabber = false;
	boolean dontTurn = false;

	float armRotation = 0;
	float grabRotation = 0;

	/**
	 * Constructor
	 */
	public XDrive1Test() {
		telemetry.addData("Beep", String.format("Boop"));

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For this test, we assume the following,
		 *   There are four motors
		 *   "fl" and "bl" are front and back left wheels
		 *   "fr" and "br" are front and back right wheels
		 */
		try {
			motorFrontRight = hardwareMap.dcMotor.get("fr");
		}
		catch (IllegalArgumentException iax) {
			bDebugFR = true;
		}
		try{
			motorFrontLeft = hardwareMap.dcMotor.get("fl");
			motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		}
		catch (IllegalArgumentException iax) {
			bDebugFL = true;
		}
		try{
			motorBackRight = hardwareMap.dcMotor.get("br");
		}
		catch (IllegalArgumentException iax) {
			bDebugBR = true;
		}
		try{
			motorBackLeft = hardwareMap.dcMotor.get("bl");
			motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
		}
		catch (IllegalArgumentException iax) {
			bDebugBL = true;
		}
		/*
		try{
			//motorArm = hardwareMap.dcMotor.get("arm");
		}
		catch(IllegalArgumentException iax){
			bDebugArm = true;
		}

		 */
		try{
			servoGrabber = hardwareMap.servo.get("grab");
		}
		catch(IllegalArgumentException iax){
			bDebugGrabber = true;
		}
	}

	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		// tank drive
		// note that if y equal -1 then joystick is pushed all of the way forward.
		float x1 = gamepad1.left_stick_x;
		float y1 = -gamepad1.left_stick_y;
		float r1 = gamepad1.right_stick_x;
		float y2 = -gamepad2.left_stick_y;
		float y3 = -gamepad2.right_stick_y;

		//lt = half speed
		float lt = gamepad1.left_trigger;
		//rt = 2x speed
		float rt = gamepad1.right_trigger;

		// clip the right/left values so that the values never exceed +/- 1
		x1 = Range.clip(x1, -1, 1);
		y1 = Range.clip(y1, -1, 1);
		r1 = Range.clip(r1, -1, 1);
		y2 = Range.clip(y2, -1, 1);
		y3 = Range.clip(y3, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		x1 = (float)scaleInput(x1);
		y1 = (float)scaleInput(y1);
		r1 = (float)scaleInput(r1);
		y2 = (float)scaleInput(y2);
		y3 = (float)scaleInput(y3);

		float fr = (y1-x1)-r1;
		float br = (y1+x1)-r1;
		float fl = (y1+x1+r1);
		float bl = (y1-x1)+r1;

		float armRotationIncrement = y3;
		armRotation += armRotationIncrement;

		float grabRotationIncrement = y2/10;
		grabRotation += grabRotationIncrement;


		if(Math.abs(fr) > 1 && Math.abs(fr) >= Math.abs(br) && Math.abs(fr) >= Math.abs(fl) && Math.abs(fr) >= Math.abs(bl)){
			br = br / Math.abs(fr);
			fl = fl / Math.abs(fr);
			bl = bl / Math.abs(fr);
			fr = fr / Math.abs(fr);
		}
		else if(Math.abs(br) > 1 && Math.abs(br) >= Math.abs(fr) && Math.abs(br) >= Math.abs(fl) && Math.abs(br) >= Math.abs(bl)){
			fr = fr / Math.abs(br);
			fl = fl / Math.abs(br);
			bl = bl / Math.abs(br);
			br = br / Math.abs(br);
		}
		else if(Math.abs(fl) > 1 && Math.abs(fl) >= Math.abs(fr) && Math.abs(fl) >= Math.abs(br) && Math.abs(fl) >= Math.abs(bl)){
			fr = fr / Math.abs(fl);
			br = br / Math.abs(fl);
			bl = bl / Math.abs(fl);
			fl = fl / Math.abs(fl);
		}
		else if(Math.abs(bl) > 1 && Math.abs(bl) >= Math.abs(fr) && Math.abs(bl) >= Math.abs(br) && Math.abs(bl) >= Math.abs(fl)){
			fr = fr / Math.abs(bl);
			br = br / Math.abs(bl);
			fl = fl / Math.abs(bl);
			bl = bl / Math.abs(bl);
		}

		if(armRotation >= 10){
			armRotation = 10;
			dontTurn = true;
		}
		else if(armRotation <= 0){
			armRotation = 0;
			dontTurn = true;
		}
		else{
			dontTurn = false;
		}

		if(grabRotation > .5f){
			grabRotation = .5f;
		}
		else if(grabRotation < 0){
			grabRotation = 0;
		}

		fr /= 2;
		br /= 2;
		fl /= 2;
		bl /= 2;

		fr *= (1-(lt/2));
		br *= (1-(lt/2));
		fl *= (1-(lt/2));
		bl *= (1-(lt/2));

		fr *= (1+rt);
		br *= (1+rt);
		fl *= (1+rt);
		bl *= (1+rt);

		float arm = y3/10;

		// write the values to the motors - for now, front and back motors on each side are set the same
		if (!bDebugFR || !bDebugBR || !bDebugFL || !bDebugBL) {
			if(fr == 0){
				motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			else{
				motorFrontRight.setPower(fr);
			}
			if(br == 0){
				motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			else{
				motorBackRight.setPower(br);
			}
			if(fl == 0){
				motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			else{
				motorFrontLeft.setPower(fl);
			}
			if(bl == 0){
				motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			else{
				motorBackLeft.setPower(bl);
			}
/*
			if(dontTurn = true || arm == 0){
				motorArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
			else if(dontTurn = false){
				motorArm.setPower(arm);
			}
*/
			servoGrabber.setPosition(grabRotation);

      /*
      if(r != 0) {
         x = 0;
         y = 0;
         motorFrontRight.setPower(-r);
         motorBackRight.setPower(-r);
         motorFrontLeft.setPower(r);
         motorBackLeft.setPower(r);
      }
       */
		}

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		telemetry.addData("Text", "*** Test v1.0 ***");

		/*
		 * Checks for each wheel's power, and if the wheel setup ran into an error,
		 * will return 'not working' instead of a power.
		 */
		if(!bDebugFR){
			telemetry.addData("front right pwr", String.format("%.2f", fr));
		}
		else{
			telemetry.addData("front right pwr", String.format("not working"));
		}
		if(!bDebugBR){
			telemetry.addData("back right pwr", String.format("%.2f", br));
		}
		else{
			telemetry.addData("back right pwr", String.format("not working"));
		}
		if(!bDebugFL){
			telemetry.addData("front left pwr", String.format("%.2f", fl));
		}
		else{
			telemetry.addData("front left pwr", String.format("not working"));
		}
		if(!bDebugBR){
			telemetry.addData("back left pwr", String.format("%.2f", bl));
		}
		else{
			telemetry.addData("back left pwr", String.format("not working"));
		}
		telemetry.addData("left trigger", String.format("%.2f",lt));
		telemetry.addData("right trigger", String.format("%.2f",rt));
		telemetry.addData("gamepad1", gamepad1);

		if(!bDebugGrabber){
			telemetry.addData("grabber rotation", String.format("%.2f",grabRotation));
		}
		else{
			telemetry.addData("grabber rotation", String.format("not working"));
		}
		if(!bDebugArm){
			telemetry.addData("arm rotation", String.format("%.2f",armRotation));
		}
		else{
			telemetry.addData("arm rotation", String.format("&.2f",grabRotation));
		}
		telemetry.addData("gamepad2", gamepad2);
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	/*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		return dVal*dVal*dVal;    // maps {-1,1} -> {-1,1}
	}

}

//Bruh





