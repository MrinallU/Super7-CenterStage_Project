package org.firstinspires.ftc.teamcode.VISION;

import org.firstinspires.ftc.vision.VisionProcessor;

import android.graphics.Canvas;


import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Scanner;

public class BluePropThreshold implements VisionProcessor {
    Mat testMat = new Mat();

    Mat finalMat = new Mat();
    double redThreshold = 0.5;
    double leftBox = 0, rightBox = 0;

    String outStr = "left"; //Set a default value in case vision does not work

    static final Rect LEFT_RECTANGLE = new Rect(
            new Point(212, 256),
            new Point(245, 264)
    );

    static final Rect RIGHT_RECTANGLE = new Rect(
            new Point(474, 248),
            new Point(510, 260)
    );

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, testMat, Imgproc.COLOR_RGB2HSV);


        Scalar lowHSVRedLower = new Scalar(0, 100, 20);  //Beginning of Color Wheel
        Scalar lowHSVRedUpper = new Scalar(10, 255, 255);

        Scalar redHSVRedLower = new Scalar(160, 100, 20); //Wraps around Color Wheel
        Scalar highHSVRedUpper = new Scalar(180, 255, 255);

        Scalar lowBlue = new Scalar(100, 100, 20);
        Scalar highBlue = new Scalar(140, 255, 255);





        Core.inRange(testMat, lowBlue, highBlue, finalMat);

        testMat.release();





        double leftBox = Core.sumElems(finalMat.submat(LEFT_RECTANGLE)).val[0];
        double rightBox = Core.sumElems(finalMat.submat(RIGHT_RECTANGLE)).val[0];

        double averagedLeftBox = leftBox / LEFT_RECTANGLE.area() / 255;
        double averagedRightBox = rightBox / RIGHT_RECTANGLE.area() / 255; //Makes value [0,1]




        if(averagedLeftBox > redThreshold){        //Must Tune Red Threshold
            outStr = "left";
        }else if(averagedRightBox> redThreshold){
            outStr = "center";
        }else{
            outStr = "right";
        }
        rightBox = averagedRightBox;
        leftBox = averagedLeftBox;

        finalMat.copyTo(frame); //This line should only be added in when you want to see your custom pipeline
        //on the driver station stream, do not use this permanently in your code as
        // you use the "frame" mat for all of your pipelines, such as April Tag Pipelines.
        return null;




    }


    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    public String getPropPosition(){  //Added In
        return outStr;
    }

    public double[] getBoxVals(){
        return new double[]{rightBox, leftBox};
    }
}


