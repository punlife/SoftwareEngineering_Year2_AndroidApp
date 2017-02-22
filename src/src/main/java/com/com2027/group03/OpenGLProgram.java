package com.com2027.group03;

import android.opengl.GLES20;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLProgram {
    private int program;

    public class CompileException extends Exception {
        public CompileException(String msg){
            super(msg);
        }
    }

    public OpenGLProgram(){

    }

    public OpenGLProgram(final String vertexCode, final String fragmentCode) throws CompileException{
        create(vertexCode, fragmentCode);
    }

    public boolean isCreated(){
        return (program != 0);
    }

    public int getHandle(){
        return program;
    }

    public void create(final String vertexCode, final String fragmentCode) throws CompileException {
        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram();
        if(program == 0){
            throw new CompileException("Failed to create GL program!");
        }

        int vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentCode);

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertex);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragment);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            String log = GLES20.glGetProgramInfoLog(program);
            GLES20.glDeleteProgram(program);
            program = 0;
            throw new CompileException("Could not link OpenGL program " + log);
        }
    }

    private int loadShader(int type, final String code) throws CompileException {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String log = GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            shader = 0;
            throw new CompileException("Could not compile OpenGL shader " + type + ":" + log);
        }

        return shader;
    }
}
