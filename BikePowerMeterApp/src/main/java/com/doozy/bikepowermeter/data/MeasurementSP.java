package com.doozy.bikepowermeter.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristina on 10.01.2018.
 */

public class MeasurementSP {
    private List<Double> speeds;
    private List<Double> powers;

    public MeasurementSP() {
        this.speeds = new ArrayList<Double>();
        this.powers = new ArrayList<Double>();
    }

    public void addSpeeds(List<Double> speeds){
        this.speeds=speeds;
    }
    public void addPowers(List<Double> powers){
        this.powers=powers;
    }

    public void addSpeed(Double speed){
        speeds.add(speed);
    }

    public void addPower(Double power){
        powers.add(power);
    }

    public void cleanMeasurementOnStop(){
        speeds=null;
        powers=null;
    }

    public String averageSpeed(){
        double average=0;
        for (Double s: speeds) {
            average+=s;
        }
        return average/speeds.size()+" m/s";
    }

    public String averagePower(){
        double average=0;
        for (Double p: powers) {
            average+=p;
        }
        return average/powers.size()+" watts";
    }


}
