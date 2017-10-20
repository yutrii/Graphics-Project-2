out vec3 N;
out vec4 v;

void main(void){
    v = gl_ModelViewMatrix * gl_Vertex;
    N = vec3(normalize(gl_NormalMatrix * gl_Normal));
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}