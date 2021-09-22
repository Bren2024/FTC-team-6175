package org.firstinspires.ftc.robotcontroller.external.samples;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous Left Skystone", group="Pushbot")
//@Disabled

//v. 1.2
public class AutonomousLeftSkystone extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot         robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    
    ColorSensor cs_right; 

    public ColorSensor colorSensor = null;
    
    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to Start!");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        float hsvValues[] = {0F,0F,0F};
        
        final float values[] = hsvValues;
        
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
        
        colorSensor = hardwareMap.colorSensor.get("cs_right");
        
        // Step 1:  Drive forward for 10 seconds
        robot.leftDrive.setPower(.75);
        robot.rightDrive.setPower(.85);
        robot.middleDrive.setPower(0);
        robot.armServo.setPower(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.09)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Getting into Position: Part 2", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.middleDrive.setPower(0);
        robot.elevator.setPower(-1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.7)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Grabbing Skystone: Part 1", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.elevator.setPower(0);
        robot.grabServo.setPosition(-1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Grabbing Skystone: Part 2", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.armServo.setPower(0);
        robot.elevator.setPower(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.95)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Grabbing Skystone: Part 3", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.grabServo.setPosition(1);
        robot.elevator.setPower(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Grabbing Skystone: Part 3", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.armServo.setPower(-1);
        robot.leftDrive.setPower(-1.0);
        robot.rightDrive.setPower(-1.0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.2)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Taking Skystone: Part 1", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.armServo.setPower(0);
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.middleDrive.setPower(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Taking Skystone: Part 2", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.grabServo.setPosition(-1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Dropping Skystone", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.middleDrive.setPower(-1);
        robot.grabServo.setPosition(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Going Under Bridge", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.armServo.setPower(-1);
        robot.middleDrive.setPower(0);
        robot.leftDrive.setPower(1);
        robot.rightDrive.setPower(1);
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Going Under Bridge", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.grabServo.setPosition(1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 4.5)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Going Under Bridge", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.middleDrive.setPower(0);
        telemetry.addData("Path", "Done!");
        telemetry.update();

    }
}
