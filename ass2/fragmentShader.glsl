in vec3 N;
in vec4 v;

void main(void){
   vec3 normal, lightDir;
   vec4 diffuse, globalAmbient,ambient;
   float NdotL;

   globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
   ambient = gl_LightSource[0].ambient * gl_FrontMaterial.ambient;

   normal = normalize(N);
   
   //Direction from fragment to the light
   lightDir = normalize(vec3(gl_LightSource[0].position - v));

   
   diffuse =  gl_LightSource[0].diffuse * gl_FrontMaterial.diffuse * (0.5 * dot(normal,lightDir) + 0.5);
   

   gl_FragColor = globalAmbient + ambient + diffuse;

}