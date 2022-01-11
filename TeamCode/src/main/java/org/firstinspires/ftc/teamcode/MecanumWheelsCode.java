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

import java.lang.Math;
import java.util.*;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="Mecanum Wheels", group="Linear Opmode")
//@Disabled
public class MecanumWheelsCode extends LinearOpMode {

    // Declare hardware variables
    HardwarePushbot robot = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static double xAxis;
    static double yAxis;

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.init(hardwareMap);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        Dictionary<String,Boolean> prevControllerInput = new Hashtable<>();

        boolean driveOrientation = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            prevControllerInput.put("A Button", gamepad1.a);
            prevControllerInput.put("B Button", gamepad1.b);

            double frontLeftPower;
            double frontRightPower;
            double backLeftPower;
            double backRightPower;
            double rightTrigger;
            double rightTriggerPower;
            double collectionPower;
            double carouselPower;
            double armPower;
            double liftServoPower;
            double leftStickX;
            double leftStickY;
            double robotRotation;

            // Simplification of: If the a button has been pressed since the last loop, then change the driveOrientation variable.
            driveOrientation = ((prevControllerInput.get("A Button") != gamepad1.a) && gamepad1.a) != driveOrientation;

            // Set up the math to run the Mecanum Wheels
            leftStickX = gamepad1.left_stick_x;
            leftStickY = gamepad1.left_stick_y;
            if (driveOrientation) {
                robotRotation = angles.firstAngle * Math.PI / 180;
                xAxis = (leftStickX * Math.cos(robotRotation)) - (leftStickY * Math.sin(robotRotation)) * 1.5; // Counteract imperfect strafing. Up to driver preference.
                yAxis = (leftStickX * Math.sin(robotRotation)) + (leftStickY * Math.cos(robotRotation));
            } else {
                xAxis = gamepad1.left_stick_x * 1.5; // Counteract imperfect strafing. Up to driver preference.
                yAxis = -gamepad1.left_stick_y;
            }

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

            // If (first button) is pressed, set power to 1. If (second button) is pressed, set power to -1.
            // Else, set power to 0.
            collectionPower = gamepad2.dpad_right ? 1 : gamepad2.dpad_left ? -1 : 0;
            armPower = gamepad2.dpad_up ? 1 : gamepad2.dpad_down ? -1 : 0;
            carouselPower = gamepad2.a ? 1.5 : gamepad2.y ? -1.5 : 0;
            if gamepad2.a

            // Init the rightTrigger variable.
            rightTrigger = gamepad1.right_trigger;

            // Slow down the robot if the right trigger is pressed.
            rightTriggerPower = rightTrigger + 1;
            frontLeftPower /= rightTriggerPower;
            frontRightPower /= rightTriggerPower;
            backLeftPower /= rightTriggerPower;
            backRightPower /= rightTriggerPower;

            prevControllerInput.put("A Button", gamepad1.a);

            // Send calculated power to wheels
            robot.frontLeftDrive.setPower(frontLeftPower);
            robot.frontRightDrive.setPower(frontRightPower);
            robot.backLeftDrive.setPower(backLeftPower);
            robot.backRightDrive.setPower(backRightPower);
            robot.carouselMotor.setPower(carouselPower);
            robot.arm.setPower(armPower);
            robot.liftServo.setPower();

            robot.leftCollection.setPower(collectionPower);
            robot.rightCollection.setPower(collectionPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "Front Left: (%.2f), Front Right (%.2f), Back Left (%.2f), Back Right (%.2f)", frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Motors", "Collection: (%.2f)", collectionPower);
            telemetry.addData("Position", "Heading: (%.2f)", angles.firstAngle);

            telemetry.update();
        }
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
