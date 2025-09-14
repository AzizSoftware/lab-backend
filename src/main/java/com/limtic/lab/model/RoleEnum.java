package com.limtic.lab.model;

public enum RoleEnum {
    VISITOR,      // Registered user with minimal access
    USER,         // Simple authenticated user
    PERMANENT,    // Professors, researchers, permanent staff
    ADMIN,        // Admin with management permissions
    SUPER_ADMIN   // Super admin with full control
}
