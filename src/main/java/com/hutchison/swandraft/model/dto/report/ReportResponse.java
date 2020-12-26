package com.hutchison.swandraft.model.dto.report;

import com.hutchison.swandraft.model.tournament.round.result.ResultState;
import lombok.NonNull;
import lombok.Value;

@Value
public class ReportResponse {
    ResultState state;
    String message;

    private ReportResponse(@NonNull ResultState state,
                           @NonNull String message) {
        this.state = state;
        this.message = message;
    }

    public static ReportResponse fromState(ResultState state) {
        return new ReportResponse(state, getMessage(state));
    }

    private static String getMessage(ResultState state) {
        switch (state) {
            case CONFIRMED:
                return "Report recorded and confirmed.";
            case UNCONFIRMED:
                return "Report recorded. Waiting for opponent to confirm.";
            case REVERSED:
                return "Report conflicted with opponent's report. Both have been deleted, please report again.";
            case ERROR:
                return "Report either conflicts with a previous report or player has received a bye.";
            default:
                throw new IllegalArgumentException("Invalid Result state.");
        }
    }
}
