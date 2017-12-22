package com.doozy.bikepowermeter.data;

/**
 * Created by Kristina on 29.11.2017
 */

public class Item {
    protected String date;
    protected String power;
    protected String speed;
    protected String duration;

    public Item(String date, String power, String speed, String duration) {
        this.date = date;
        this.power = power;
        this.speed = speed;
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
/*

    // Calculates and returns the force components needed to achieve the
    // given velocity.  <params> is a dictionary containing the rider and
    // environmental parameters, as returned by ScrapeParameters(), i.e.,
    // all in metric units.  'velocity' is in km/h.
    function CalculateForces(velocity, params) {
        // calculate Fgravity
        double Fgravity = 9.8067 *
                (params.rp_wr + params.rp_wb) *
                Math.sin(Math.atan(params.ep_g / 100.0));

        // calculate Frolling
        double Frolling = 9.8067 *
                (params.rp_wr + params.rp_wb) *
                Math.cos(Math.atan(params.ep_g / 100.0)) *
                (params.ep_crr);

        // calculate Fdrag
        double Fdrag = 0.5 *
                (params.rp_a) *
                (params.rp_cd) *
                (params.ep_rho) *
                (velocity * 1000.0 / 3600.0) *
                (velocity * 1000.0 / 3600.0);

        // cons up and return the force components
        double ret = {};
        ret.Fgravity = Fgravity;
        ret.Frolling = Frolling;
        ret.Fdrag = Fdrag;
        return ret;
    }

    // Calculates and returns the power needed to achieve the given
    // velocity.  <params> is a dictionary containing the rider and
    // environmenetal parameters, as returned by ScrapeParameters(), i.e.,
    // all in metric units.  'velocity' is in km/h.  Returns power in
    // watts.
    function CalculatePower(velocity, params) {
        // calculate the forces on the rider.
        double forces = CalculateForces(velocity, params);
        double totalforce = forces.Fgravity + forces.Frolling + forces.Fdrag;

        // calculate necessary wheelpower
        double wheelpower = totalforce * (velocity * 1000.0 / 3600.0);

        // calculate necessary legpower
        double legpower = wheelpower / (1.0 - (params.rp_dtl / 100.0));

        return legpower;
    }

    // Calculates the velocity obtained from a given power.  <params> is a
    // dictionary containing the rider and model parameters, all in
    // metric units.
    //
    // Runs a simple midpoint search, using CalculatePower().
    //
    // Returns velocity, in km/h.
    function CalculateVelocity(power, params) {
        // How close to get before finishing.
        double epsilon = 0.000001;

        // Set some reasonable upper / lower starting points.
        double lowervel = -1000.0;
        double uppervel = 1000.0;
        double midvel = 0.0;

        double midpow = CalculatePower(midvel, params);

        // Iterate until completion.
        double itcount = 0;
        do {
            if (Math.abs(midpow - power) < epsilon)
                break;

            if (midpow > power)
                uppervel = midvel;
            else
                lowervel = midvel;

            midvel = (uppervel + lowervel) / 2.0;
            midpow = CalculatePower(midvel, params);
        } while (itcount++ < 100);

        return midvel;
    }
*/


    @Override
    public String toString() {
        return String.format("Avg. Power  Avg. Speed   Duration \n\t %10s %12s %13s", power, speed, duration);
    }
}
