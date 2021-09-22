// package org.firstinspires.ftc.teamcode;
package org.firstinspires.ftc.robotcontroller.external.samples;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous Right Inside", group="Pushbot")
//@Disabled

//v. 1.2
public class AutonomousRightInside extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot         robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    
    ColorSensor cs_left; 
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
        
        colorSensor = hardwareMap.colorSensor.get("cs_left");
        
        // Step 1:  Drive forward for 10 seconds
        robot.leftDrive.setPower(0.5);
        robot.rightDrive.setPower(0.5);
        robot.middleDrive.setPower(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.8)) {
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Going To Site", runtime.seconds());
            telemetry.addData("Color", "Hue: %.2f", hsvValues[0]);
            telemetry.update();
        }
        
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.middleDrive.setPower(-1);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.7)) { 
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);
            telemetry.addData("Path", "Going To Site", runtime.seconds());
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