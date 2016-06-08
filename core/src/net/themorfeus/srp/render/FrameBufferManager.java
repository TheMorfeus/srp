package net.themorfeus.srp.render;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.util.Stack;


/**
 * courtesy of stackoverflow user kajacx*/
public class FrameBufferManager {

    private static Stack<FrameBuffer> stack = new Stack<FrameBuffer>();

    public static void begin(FrameBuffer buffer) {
        if (!stack.isEmpty()) {
            stack.peek().end();
        }
        stack.push(buffer).begin();
    }

    public static void end() {
        stack.pop().end();
        if (!stack.isEmpty()) {
            stack.peek().begin();
        }
    }

}