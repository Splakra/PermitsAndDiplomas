package net.splakra.permitsanddiplomas.util;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector3f;

public class ServerUtils {

    public static void spawnRing(ServerPlayer player, double radius, int points) {
        ServerLevel world = player.serverLevel();
        double y = player.getY() + 1.5;
        Vector3f color = new Vector3f(1.0f, 0.9f, 0.0f);

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = player.getX() + Math.cos(angle) * radius;
            double z = player.getZ() + Math.sin(angle) * radius;

            world.sendParticles(
                    new DustParticleOptions(color, 1),
                    x, y, z,
                    1,      // one per point
                    0, 0, 0,
                    0       // no extra speed, it’s already a burst
            );
        }
    }
}
