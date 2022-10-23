package me.moose.gaia.master.profile.crash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CrashReport {
    private String id;
    private String version;
    private String os;
    private String memory;
    private String stackTrace;

    @Override
    public String toString() {
        return "CrashReport{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", os='" + os + '\'' +
                ", memory='" + memory + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
