package dev.o8o1o5.terrariaaccessories.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CurioManager {
    // 공통 상태 (아이템 간 공유)
    public static final Map<UUID, Boolean> usedCloud = new HashMap<>();
    public static final Map<UUID, Boolean> usedRocket = new HashMap<>();
    public static final Map<UUID, Boolean> usedSpectreRocket = new HashMap<>();
    public static final Map<UUID, Boolean> hasCloudJar = new HashMap<>();
    public static final Map<UUID, Boolean> jumpHandledByCloudJar = new HashMap<>();

    // ticksInAir과 jumpKeyWasDown은 이제 아이템 내부에서 독립적으로 관리하므로 제거
    // public static final Map<UUID, Integer> ticksInAir = new HashMap<>();
    // public static final Map<UUID, Boolean> jumpKeyWasDown = new HashMap<>();
}
