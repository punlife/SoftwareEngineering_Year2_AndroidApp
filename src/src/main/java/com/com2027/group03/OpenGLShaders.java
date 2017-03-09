package com.com2027.group03;

import android.opengl.GLES20;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLShaders {
    public static final String spriteShaderVert =
            "attribute vec2 vert;\n" +
            "uniform mat4 proj;\n" +
            "uniform mat4 model;\n" +
            "uniform vec2 size;\n" +
            "uniform vec4 pos;\n" +
            "varying vec2 texCoord;\n" +
            "void main() {\n" +
            "  texCoord = vert + vec2(0.5, 0.5);\n" +
            "  vec4 rotated = model * vec4(vert * size.xy + pos.zw, 0.0, 1.0);\n" +
            "  gl_Position = proj * vec4(rotated.xy + pos.xy, 0.0, 1.0);\n" +
            "}\n";

    public static final String spriteShaderFrag =
            "precision mediump float;\n" +
            "varying vec2 texCoord;\n" +
            "uniform vec4 color;\n" +
            "uniform vec4 sub;\n" +
            "uniform sampler2D tex;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(tex, texCoord * sub.zw + sub.xy) * color;\n" +
            "  //gl_FragColor = vec4(texCoord * sub.zw + sub.xy, 0.0, 1.0);\n" +
            "}\n";

    public static class SpriteShader extends OpenGLProgram {
        public int uniformPosLoc = 0;
        public int uniformProjLoc = 0;
        public int uniformSizeLoc = 0;
        public int uniformColorLoc = 0;
        public int uniformVertLoc = 0;
        public int uniformTexLoc = 0;
        public int uniformSubLoc = 0;
        public int uniformModelLoc = 0;

        public SpriteShader() throws OpenGLProgram.CompileException {
            super(OpenGLShaders.spriteShaderVert, OpenGLShaders.spriteShaderFrag);

            uniformVertLoc = GLES20.glGetAttribLocation(this.getHandle(), "vert");
            uniformProjLoc = GLES20.glGetUniformLocation(this.getHandle(), "proj");
            uniformModelLoc = GLES20.glGetUniformLocation(this.getHandle(), "model");
            uniformSizeLoc = GLES20.glGetUniformLocation(this.getHandle(), "size");
            uniformColorLoc = GLES20.glGetUniformLocation(this.getHandle(), "color");
            uniformPosLoc = GLES20.glGetUniformLocation(this.getHandle(), "pos");
            uniformTexLoc = GLES20.glGetUniformLocation(this.getHandle(), "tex");
            uniformSubLoc = GLES20.glGetUniformLocation(this.getHandle(), "sub");
        }
    }
}
