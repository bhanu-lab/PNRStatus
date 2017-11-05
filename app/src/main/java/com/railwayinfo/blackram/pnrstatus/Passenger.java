package com.railwayinfo.blackram.pnrstatus;

/**
 * Created by blackram on 18/02/17.
 */

public class Passenger {

    private String bookingStatus;
    private String currentStatus;
    private String coachPosition;
    private String passengerId;


    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getCoachPosition() {
        return coachPosition;
    }

    public void setCoachPosition(String coachPosition) {
        this.coachPosition = coachPosition;
    }


    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }



    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString(){
        return "Passenger "+passengerId+"\n"+"BookingStatus: "+bookingStatus+"\n"+"CurrentStatus: "+currentStatus+"\n";
    }
}
