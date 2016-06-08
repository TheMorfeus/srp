package net.themorfeus.srp.render;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.util.Stack;


/**
 * Used for drawing to multiple frame buffers in one frame.
 * @author kajacx
 * */
public class FrameBufferManager {

    private static Stack<FrameBuffer> stack = new Stack<FrameBuffer>();

    /**
     * Begins drawing to the given {@link FrameBuffer}, ending any other FrameBuffer that might be active at the time
     * */
    public static void begin(FrameBuffer buffer) {
        if (!stack.isEmpty()) {
            stack.peek().end();
        }
        stack.push(buffer).begin();
    }

    /**
     * Ends drawing to the given {@link FrameBuffer}, beginning any other FrameBuffer that might be in queue
     * */
    public static void end() {
        stack.pop().end();
        if (!stack.isEmpty()) {
            stack.peek().begin();
        }
    }

}