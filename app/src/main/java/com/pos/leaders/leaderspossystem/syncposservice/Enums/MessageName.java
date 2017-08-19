package com.pos.leaders.leaderspossystem.syncposservice.Enums;

/**
 * Created by KARAM on 27/07/2017.
 */

public enum MessageName {

    MessageType {
        @Override
        public String toString() {
            return "MessageType";
        }
    } ,
    Data {
        @Override
        public String toString() {
            return "Data";
        }
    } ,
    Result {
        @Override
        public String toString() {
            return "Result";
        }
    }
}
