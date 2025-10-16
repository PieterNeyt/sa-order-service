package be.kdg.sa.backend.domain.order;

public enum OrderState {
    NOT_PLACED,
    PLACED,
    ACCEPTED,
    DENIED,
    READY_FOR_PICKUP,
    PICKED_UP,
    DELIVERD
}
