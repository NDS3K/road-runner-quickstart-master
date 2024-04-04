package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp")
public class TeleOpMode extends LinearOpMode {
    public static class Drivetrain {
        public DcMotorEx topLeft,backLeft,topRight,backRight ;
        public Drivetrain(HardwareMap hardwareMap){

            topLeft = hardwareMap.get(DcMotorEx.class,"tl");
            backLeft = hardwareMap.get(DcMotorEx.class,"bl");
            backRight = hardwareMap.get(DcMotorEx.class,"br");
            topRight = hardwareMap.get(DcMotorEx.class,"tr");

            topRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            topLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            topLeft.setDirection(DcMotorEx.Direction.FORWARD);
            backLeft.setDirection(DcMotorEx.Direction.FORWARD);
            topRight.setDirection(DcMotorEx.Direction.REVERSE);
            backRight.setDirection(DcMotorEx.Direction.REVERSE);
        }
        public void drive(double x , double y, double turn){
            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta -Math.PI/4);
            double cos = Math.cos(theta -Math.PI/4);

            double max = Math.max(Math.abs(sin),Math.abs(cos));
            double topLeftPower = power*cos/max + turn;
            double topRightPower = power*sin/max - turn;
            double backLeftPower = power*sin/max + turn;
            double backRightPower = power*cos/max - turn;

            topLeft.setPower(topLeftPower);
            topRight.setPower(topRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }

    }

    public static class HandCore{
        public DcMotorEx elbowDrive;
        public Servo grip1 ;
        public Servo grip2;
        public CRServo wrist;
        public HandCore(HardwareMap hardwareMap){
            elbowDrive = hardwareMap.get(DcMotorEx.class,"eb");
            grip1 = hardwareMap.get(Servo.class,"gr1");
            grip2 = hardwareMap.get(Servo.class,"gr2");
            wrist = hardwareMap.get(CRServo.class,"wr");

            elbowDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        public void move(double x,boolean w,boolean p, boolean y,boolean z){
            elbowDrive.setPower(x);
            if(w) {
                wrist.setPower(0.7);
            }else {
                wrist.setPower(0);
            }
            if(p){
                wrist.setPower(-0.7);
            }else{
                wrist.setPower(0);
            }

            if(y){
                grip1.setPosition(0.5);
                grip2.setPosition(0.5);
            }
            else if (z){
                grip2.setPosition(1);
                grip1.setPosition(0);
            }

        }
    }
    public void runOpMode(){
        Drivetrain drivetrain = new Drivetrain(hardwareMap);
        HandCore handCore = new HandCore(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            drivetrain.drive(gamepad1.left_stick_x,-gamepad1.left_stick_y,gamepad1.right_stick_x);
            handCore.move(gamepad2.left_stick_y,gamepad2.y,gamepad2.a,gamepad2.x,gamepad2.b);
        }
    }
}


