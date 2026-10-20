package com.unibank.audit.dto;

// Reference list of known audit actions. Not enforced by the entity/DB —
// kept as plain strings so any service can log an action without a shared
// dependency. This class exists purely so the exact spelling is documented
// in one place; copy these constants when calling POST /api/audit/log
// from other services.
public final class AuditAction {
    public static final String USER_REGISTERED = "USER_REGISTERED";
    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String ACCOUNT_LINKED = "ACCOUNT_LINKED";
    public static final String CONSENT_CREATED = "CONSENT_CREATED";
    public static final String CONSENT_REVOKED = "CONSENT_REVOKED";
    public static final String BALANCE_VIEWED = "BALANCE_VIEWED";
    public static final String TRANSACTIONS_VIEWED = "TRANSACTIONS_VIEWED";
    public static final String PAYMENT_CREATED = "PAYMENT_CREATED";
    public static final String PAYMENT_HELD = "PAYMENT_HELD";
    public static final String FD_CLOSURE_REQUESTED = "FD_CLOSURE_REQUESTED";
    public static final String ACCOUNT_CLOSURE_REQUESTED = "ACCOUNT_CLOSURE_REQUESTED";

    private AuditAction() {}
}