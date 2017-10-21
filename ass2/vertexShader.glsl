#version 130

out vec3 N; 
out vec4 v; 
out vec2 tex;

void main (void) {	
    v = gl_ModelViewMatrix * gl_Vertex;
    N = vec3(normalize(gl_NormalMatrix * normalize(gl_Normal)));
	tex = vec2(gl_MultiTexCoord0);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;	
}