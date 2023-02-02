package com.acem.demo.constant;

public interface ResponseMessageConstant {

    interface Batch{
        String FOUND = "Found Successfully";
        String NOT_FOUND = "Did not find successfully";
        String SAVED = "Batch Saved Successfully.";
        String NOT_SAVED = "Batch did not save.";

        String UPDATED = "Record updated successfully";
        String NOT_UPDATED = "Record failed to be updated.";

    }

    interface Viber{
        String HANDLED_EVENT = "Event handled successfully";
        String HANDLED_EVENT_FAILED = "Error occurred. Couldn't handle event successfully.";
        String MESSAGE_SENT = "Message sent successfully";

        String CONVERSATION_STARTED_CALLBACK_SENT = "Conversation started callback message sent " +
                "successfully.";

        String CONVERSATION_STARTED_CALLBACK_FAILED = "Conversation started callback message " +
                "couldn't be sent.";
        String MESSAGE_FAILED = "Message failed to send.";
        String INVALID_MESSAGE = "The message you sent is invalid.\nPlease check the syntax.\n" +
                "The syntax is: Course-Year-Section-Day\nFor example: BEI-III-A-SUNDAY\nYou can " +
                "also write:\n" +
                "BEI-III-A-TODAY => this will return the schedule for today.\n" +
                "BEI-III-A => this will return the schedule for tomorrow.\n" +
                "BEI-III-A-TOMORROW => this will also return the schedule for tomorrow.";

        String INVALID_MESSAGE_OR_RESPONSE_UNAVAILABLE = "The message you sent is invalid.\nPlease check the syntax.\n" +
                "Or the requested record may not be available.\n" +
                "Currently we only support BEI-III-A\n" +
                "Also make sure, you are not setting day to SATURDAY.";

        String WELCOME_MESSAGE = "Welcome to ACEM Routine Bot, \nReply with the following syntax " +
                "to receive your desired schedule." +
                "The syntax is: Course-Year-Section-Day\nFor example: BEI-III-A-SUNDAY\nYou can " +
                "also write:\n" +
                "BEI-III-A => this will return the schedule for tomorrow.\n" +
                "BEI-III-A-TODAY => this will return the schedule for today.\n" +
                "BEI-III-A-TOMORROW => this will return the schedule for tomorrow.";
    }
}
