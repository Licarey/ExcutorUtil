package com.example.record;

public class ThinRExtension {

    public boolean skipThinR = true
    public boolean skipThinRDebug = true
    public int logLevel = 2


    @Override
    public String toString() {
        String str =
                """
                skipThinR: ${skipThinR}
                skipThinRDebug: ${skipThinRDebug}
                logLevel: ${logLevel}
                """
        return str
    }
}
