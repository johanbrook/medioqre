package audio;

public class AudioConstants {

	public static final float zROLLOFF = 100;
	
	private float BGM_VOLUME = 1;
	private float FX_VOLUME = 1;
	
	
	public float getBGMVolume(){
		return BGM_VOLUME;
	}
	
	public void setVolume(float f){
		BGM_VOLUME = f;
	}
	
	public float getFXVolume(){
		return FX_VOLUME;
	}
	
	public void setFXVolume(float f){
		FX_VOLUME = f;
	}
	

}
