package com.movie.ticket.booking.system.commons.constants;

public enum LoggerConstants {

    ENTERED_CONTROLLER_MESSAGE("Entered {} controller of {} class with value {}"),
    EXITING_CONTROLLER_MESSAGE("Exiting {} controller of {} class"),
    ENTERED_SERVICE_MESSAGE("Entered into {} service method of {} class with value {}"),
    EXITING_SERVICE_MESSAGE("Entered into {} service method of {} class"),
    ENTERED_CONTROLLER_HANDLER_MESSAGE("Entered {} handler of {} class with value {}");

    private final String value;

    LoggerConstants(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
