package com.com2027.group03;

import android.opengl.GLES20;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLShaders {
    public static final String rectangleShaderVert =
            "attribute vec2 vert;\n" +
            "uniform mat4 proj;\n" +
            "uniform vec3 size;\n" +
            "uniform vec4 pos;\n" +
            "vec2 apply_rot(vec2 p){\n" +
            "  float s = sin(size.z * 0.01745329251); float c = cos(size.z * 0.01745329251);\n" +
            "  vec2 new;\n" +
            "  new.x = p.x * c - p.y * s;\n" +
            "  new.y = p.x * s + p.y * c;\n" +
            "  return new;\n" +
            "}\n" +
            "void main() {\n" +
            "  gl_Position = proj * vec4(apply_rot(vert * size.xy + pos.zw) + pos.xy, 0.0f, 1.0f);\n" +
            "}\n";

    public static final String rectangleShaderFrag =
            "precision mediump float;\n" +
            "uniform vec4 color;\n" +
            "void main() {\n" +
            "  gl_FragColor = color;\n" +
            "}\n";

    public static final String spriteShaderVert =
            "attribute vec2 vert;\n" +
            "uniform mat4 proj;\n" +
            "uniform vec3 size;\n" +
            "uniform vec4 pos;\n" +
            "varying vec2 texCoord;\n" +
            "uniform float rot;\n" +
            "vec2 apply_rot(vec2 p){\n" +
            "  float s = sin(size.z * 0.01745329251); float c = cos(size.z * 0.01745329251);\n" +
            "  vec2 new;\n" +
            "  new.x = p.x * c - p.y * s;\n" +
            "  new.y = p.x * s + p.y * c;\n" +
            "  return new;\n" +
            "}\n" +
            "void main() {\n" +
            "  texCoord = vert + vec2(0.5f, 0.5f);\n" +
            "  gl_Position = proj * vec4(apply_rot(vert * size.xy + pos.zw) + pos.xy, 0.0f, 1.0f);\n" +
            "}\n";

    public static final String spriteShaderFrag =
            "precision mediump float;\n" +
            "varying vec2 texCoord;\n" +
            "uniform vec4 color;\n" +
            "uniform vec4 sub;\n" +
            "uniform sampler2D tex;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(tex, texCoord * sub.zw + sub.xy) * color;\n" +
            "}\n";

    public static class RectangleShader extends OpenGLProgram {
        public int uniformPosLoc = 0;
        public int uniformProjLoc = 0;
        public int uniformSizeLoc = 0;
        public int uniformColorLoc = 0;
        public int uniformVertLoc = 0;
        public int uniformTexLoc = 0;

        public RectangleShader() throws OpenGLProgram.CompileException {
            super(OpenGLShaders.rectangleShaderVert, OpenGLShaders.rectangleShaderFrag);

            uniformVertLoc = GLES20.glGetAttribLocation(this.getHandle(), "vert");
            uniformProjLoc = GLES20.glGetUniformLocation(this.getHandle(), "proj");
            uniformSizeLoc = GLES20.glGetUniformLocation(this.getHandle(), "size");
            uniformColorLoc = GLES20.glGetUniformLocation(this.getHandle(), "color");
            uniformPosLoc = GLES20.glGetUniformLocation(this.getHandle(), "pos");
            uniformTexLoc = GLES20.glGetUniformLocation(this.getHandle(), "tex");
        }
    }

    public static class SpriteShader extends OpenGLProgram {
        public int uniformPosLoc = 0;
        public int uniformProjLoc = 0;
        public int uniformSizeLoc = 0;
        public int uniformColorLoc = 0;
        public int uniformVertLoc = 0;
        public int uniformTexLoc = 0;
        public int uniformSubLoc = 0;

        public SpriteShader() throws OpenGLProgram.CompileException {
            super(OpenGLShaders.spriteShaderVert, OpenGLShaders.spriteShaderFrag);

            uniformVertLoc = GLES20.glGetAttribLocation(this.getHandle(), "vert");
            uniformProjLoc = GLES20.glGetUniformLocation(this.getHandle(), "proj");
            uniformSizeLoc = GLES20.glGetUniformLocation(this.getHandle(), "size");
            uniformColorLoc = GLES20.glGetUniformLocation(this.getHandle(), "color");
            uniformPosLoc = GLES20.glGetUniformLocation(this.getHandle(), "pos");
            uniformTexLoc = GLES20.glGetUniformLocation(this.getHandle(), "tex");
            uniformSubLoc = GLES20.glGetUniformLocation(this.getHandle(), "sub");
        }
    }
}
