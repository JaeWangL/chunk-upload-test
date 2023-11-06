package com.smartone.medishare.shared.helpers;

import com.github.f4b6a3.ulid.UlidCreator;

public class RandomHelpers {
    public static String randomIdenfier() {
        return UlidCreator.getUlid().toString();
    }
}
