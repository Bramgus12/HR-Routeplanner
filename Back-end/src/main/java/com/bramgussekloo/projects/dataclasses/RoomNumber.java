package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.*;

@ApiModel(description = "Code for a room")
public class RoomNumber {

    @ApiModelProperty(notes = "The code for a room.")
    private String roomNumber;

    public RoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

}
