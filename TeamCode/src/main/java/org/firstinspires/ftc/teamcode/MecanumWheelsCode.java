/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Wheels", group="Linear Opmode")
//@Disabled
public class MecanumWheelsCode extends LinearOpMode {

    // Declare hardware variables
    HardwarePushbot robot = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double frontLeftPower;
            double frontRightPower;
            double backLeftPower;
            double backRightPower;
            double rightTrigger;
            double rightTriggerPower;


            // Set up the math to run the Mecanum Wheels
            double xAxis = gamepad1.left_stick_x * 1.5; // Counteract imperfect strafing. Up to driver preference.
            double yAxis = -gamepad1.left_stick_y; 
            double rotation  = gamepad1.right_stick_x;
            frontLeftPower = yAxis + xAxis + rotation;
            frontRightPower = yAxis - xAxis - rotation;
            backLeftPower = yAxis - xAxis + rotation;
            backRightPower = yAxis + xAxis - rotation;

            // Adjust motor values so that they are proportional and within the range of -1 and 1. 
            if (Math.abs(frontLeftPower) > 1 || Math.abs(backLeftPower) > 1 ||
            Math.abs(frontRightPower) > 1 || Math.abs(backRightPower) > 1 ) {
                // Find the largest power
                double max = 0;
                max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
                max = Math.max(Math.abs(frontRightPower), max);
                max = Math.max(Math.abs(backRightPower), max);

                // Divide everything by max (it's positive so we don't need to worry
                // about signs)
                frontLeftPower /= max;
                backLeftPower /= max;
                frontRightPower /= max;
                backRightPower /= max;
            }

            //leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // if (gamepad1.dpad_left) {
            //     middlePower = -1;
            // } else if (gamepad1.dpad_right) {
            //     middlePower = 1;
            // } else {
            //     middlePower = 0;
            // }
            
            // Init the rightTrigger variable.
            rightTrigger = gamepad1.right_trigger;
            
            // Slow down the robot if the right trigger is pressed. 
            rightTriggerPower = rightTrigger + 1;
            frontLeftPower /= rightTriggerPower;
            frontRightPower /= rightTriggerPower;
            backLeftPower /= rightTriggerPower;
            backRightPower /= rightTriggerPower;

            // Send calculated power to wheels
            robot.frontLeftDrive.setPower(frontLeftPower);
            robot.frontRightDrive.setPower(frontRightPower);
            robot.backLeftDrive.setPower(backLeftPower);
            robot.backRightDrive.setPower(backRightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "Front Left: (%.2f), Front Right (%.2f), Back Left (%.2f), Back Right (%.2f)", frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            // telemetry.addData("Color", "Clear: (%.2f)", colorSensor.red());
            // telemetry.addData("Colors", "Color: (%.2f)", colorSensor1);
            telemetry.update();
        }
    }
}
