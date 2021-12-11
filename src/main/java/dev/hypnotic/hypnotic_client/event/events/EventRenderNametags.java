package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class EventRenderNametags extends Event {

    private LivingEntity entity;
    private MatrixStack matrices;
    private VertexConsumerProvider vertexConsumers;

    public EventRenderNametags(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        this.entity = entity;
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public VertexConsumerProvider getVertexConsumers() {
        return vertexConsumers;
    }
}
