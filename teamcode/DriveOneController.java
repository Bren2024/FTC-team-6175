package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Drive One Controller", group="Linear Opmode")
//@Disabled
public class DriveOneController extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor middleDrive = null;
    private DcMotor elevator = null; 
    private CRServo grabServo = null;
    private CRServo armServo = null;
    private Servo buildingServo1 = null; 
    private Servo buildingServo2 = null; 


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive      = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive     = hardwareMap.get(DcMotor.class, "right_drive");
        middleDrive    = hardwareMap.get(DcMotor.class, "middle_drive");
        elevator       = hardwareMap.get(DcMotor.class, "elevator");
        grabServo      = hardwareMap.get(CRServo.class, "grab_servo");
        armServo       = hardwareMap.get(CRServo.class, "arm_servo");
        buildingServo1 = hardwareMap.get(Servo.class, "building_servo1");
        buildingServo2 = hardwareMap.get(Servo.class, "building_servo2");
        
        // colorSensor1 = hardwareMap.ColorSensor.get("colorSensor1");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        middleDrive.setDirection(DcMotor.Direction.FORWARD);
        elevator.setDirection(DcMotor.Direction.FORWARD);
        armServo.setDirection(CRServo.Direction.REVERSE); 
        
        
        // Wait for the game to start (driver presses PLAY)
        double leftPower;
        double rightPower;
        double middlePower;
        int x = 1;
        double leftTrigger;
        double rightTrigger;
        double leftWheelPower;
        double rightWheelPower;
        double middleWheelPower;
        double elevatorPower;
        double armServoPower;
        double leftTriggerPower = 1;
        boolean control = false;
        boolean rightBumper;
        boolean y = true;

        telemetry.addData("Status", "Ready To Start!");
        telemetry.update();
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // double drive = -gamepad1.left_stick_y;
            // double turn  =  gamepad1.right_stick_x;
            // leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            // rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            leftPower     = gamepad1.left_stick_y ;
            rightPower    = gamepad1.left_stick_y ;
            elevatorPower = gamepad2.left_stick_y ;
            armServoPower = -gamepad2.left_stick_x ;
            middlePower   = gamepad1.left_stick_x ;
            leftPower     = -gamepad1.right_stick_x + leftPower ;
            rightPower    = gamepad1.right_stick_x + rightPower ;
            rightTrigger  = gamepad1.right_trigger ;
            leftTrigger   = gamepad1.left_trigger ;
            
            rightBumper   = gamepad1.right_bumper ;
            
            if (gamepad1.right_trigger > 0.1) {
                grabServo.setPower(-1);
            } else {
                grabServo.setPower(1);
            }
            
            if (gamepad1.dpad_up) {
                elevator.setPower(-1);
            } else if (gamepad1.dpad_down) {
                elevator.setPower(1);
            } else {
                elevator.setPower(0);
            } 
            if (gamepad1.dpad_right) {
                armServo.setPower(-1);
            } else if (gamepad1.dpad_left) {
                armServo.setPower(1);
            } else {
                armServo.setPower(0);
            }
            
            if (gamepad1.x) {
                buildingServo1.setPosition(0);
                buildingServo2.setPosition(1);
            } else if (gamepad1.b) {
                buildingServo1.setPosition(1);
                buildingServo2.setPosition(0);
            }
            leftTriggerPower = leftTrigger + 1;
            
            leftWheelPower = leftPower/leftTriggerPower;
            rightWheelPower = rightPower/leftTriggerPower; 
            middleWheelPower = middlePower/leftTriggerPower;
            
            // Send calculated power to wheels
            leftDrive.setPower(leftWheelPower);
            rightDrive.setPower(rightWheelPower);
            middleDrive.setPower(middleWheelPower);

            y = false; 

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "Left: (%.2f), Right (%.2f), Middle (%.2f)", leftWheelPower, rightWheelPower, middleWheelPower);
            telemetry.addData("Servos", "Arm Servo: NaN");
            // telemetry.addData("Color", "Clear: (%.2f)", colorSensor.red());
            // telemetry.addData("Colors", "Color: (%.2f)", colorSensor1);
            telemetry.update();
        }
    }
}
