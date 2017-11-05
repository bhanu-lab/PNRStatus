package com.railwayinfo.blackram.pnrstatus;

import java.util.List;

/**
 * Created by blackram on 31/10/17.
 */

public class PNRStatusVO {
    private String responseCode = "";
    private String trainName = "";
    private String trainNumber = "";
    private String totalPassengersString = "";
    private String chartStatusString = "";
    private String boardingPointString = "";
    private String dojString = "";
    private String reservationUptoString = "";
    private String pnrNumber = "";
    private List<String> passengers;

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTotalPassengersString() {
        return totalPassengersString;
    }

    public void setTotalPassengersString(String totalPassengersString) {
        this.totalPassengersString = totalPassengersString;
    }

    public String getChartStatusString() {
        return chartStatusString;
    }

    public void setChartStatusString(String chartStatusString) {
        this.chartStatusString = chartStatusString;
    }

    public String getBoardingPointString() {
        return boardingPointString;
    }

    public void setBoardingPointString(String boardingPointString) {
        this.boardingPointString = boardingPointString;
    }

    public String getDojString() {
        return dojString;
    }

    public void setDojString(String dojString) {
        this.dojString = dojString;
    }

    public String getReservationUptoString() {
        return reservationUptoString;
    }

    public void setReservationUptoString(String reservationUptoString) {
        this.reservationUptoString = reservationUptoString;
    }

    @Override
    public String toString(){
        return "PNRNumber: "+pnrNumber+"\n"+"TrainNumber: "+trainNumber+" ("+trainName+")"+"\n"+"chartStatus: "+chartStatusString+"\n"+"DateOfJourney: "+dojString+"\n"+"ReservationFrom: "
                +boardingPointString+"\n"+"ReservationUpto: "+reservationUptoString+"for total passengers of "+"\n"+ totalPassengersString+" "+passengers.toString();
    }
}
