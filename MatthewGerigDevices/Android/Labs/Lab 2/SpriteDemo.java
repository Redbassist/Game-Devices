package ie.itcarlow.spritedemo;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.collision.CollisionHandler;
import org.andengine.engine.handler.collision.ICollisionCallback;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;


public class SpriteDemo extends BaseGameActivity implements IOnSceneTouchListener, IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mTexture;
	private BitmapTextureAtlas mTexture2;
	private BitmapTextureAtlas mTextureStrip;
	private ITextureRegion mBunchieLlamaTextureRegion;
	private ITextureRegion mBunchieLlama2TextureRegion;
	private ITiledTextureRegion stripTiledTextureRegion;
	private Scene mScene;
	
	private int SPR_COLUMNS;
	private int SPR_ROWS;
	
	private Numbers mNumbers;	
	private Sprite bunchieLlama;
	private Sprite bunchieLlama2;
	private TimerHandler mTimerHandler;
	
	private int index;
	
	private float previousX;
	private float previousY;
	
	private float velocityX;
	private float velocityY;
	private float length;
	
	private boolean collided;
	private boolean leftScreen;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

    @Override
	public void onCreateResources(
       OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

    	SPR_COLUMNS = 10;
    	SPR_ROWS = 1;
    	index = 0;
    	velocityX = 0;
    	velocityY = 0;
    	collided = false;
    	
    	 loadGfx();
		 pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    private void loadGfx() {     
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");  
        mTexture = new BitmapTextureAtlas(getTextureManager(), 125, 203);
        mTexture2 = new BitmapTextureAtlas(getTextureManager(), 125, 203);
        mBunchieLlamaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexture, this, "bunchie.png", 0, 0);
        mBunchieLlama2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexture2, this, "bunchieR.png", 0, 0);
        mTexture.load(); 
        mTexture2.load();

        mTextureStrip = new BitmapTextureAtlas(getTextureManager(), 500, 50);
        stripTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTextureStrip, this.getAssets(), "0to9sheet.png", 0,0,SPR_COLUMNS,SPR_ROWS);
        
        mTextureStrip.load();
    }

    @Override
  	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
  			throws Exception {

  		this.mScene = new Scene();
  		this.mScene.setBackground(new Background(0, 125, 58));	
  		//register this activity as a scene touch listener
 	   this.mScene.setOnSceneTouchListener(this);
  	    pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);  
  	}


    @Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) 
          throws Exception {

       // Setup coordinates for the sprite in order that it will
       //  be centered on the camera.
	   final float centerX = (CAMERA_WIDTH - this.mBunchieLlamaTextureRegion.getWidth()) / 2;
	   final float centerY = (CAMERA_HEIGHT - this.mBunchieLlamaTextureRegion.getHeight()) / 2;
 
	   //register activity as an update handler 
	   this.mEngine.registerUpdateHandler(this);

	   
	   // Create the sprite and add it to the scene. 

	   //final Sprite bear = new Sprite(centerX, centerY,  mAustrianBearTextureRegion, this.mEngine.getVertexBufferObjectManager());
	   //mScene.attachChild(bear);
	   bunchieLlama = new Sprite(500, 500, mBunchieLlamaTextureRegion, this.mEngine.getVertexBufferObjectManager()) 
	   {		  
		   
		   @Override
		   public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
		   {

			   previousX = this.getX();
			   previousY = this.getY();
				
			   this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, 
					   pSceneTouchEvent.getY() - this.getHeight() / 2);
			   return true;
			   
		   }	   
		   
	   };
	   
	   bunchieLlama2 = new Sprite(200,100,mBunchieLlama2TextureRegion, this.mEngine.getVertexBufferObjectManager());
	   
	   mTimerHandler = new TimerHandler(1.0f, true, new ITimerCallback() {
		   @Override
		   public void onTimePassed(TimerHandler pTimerHandler) {
			   index++;
			   if (index > 9)
					index = 0;
			   pTimerHandler.setTimerSeconds(1.0f);
			   pTimerHandler.reset();
		   }
	   });

	   this.mEngine.registerUpdateHandler(mTimerHandler);
	   
	   ICollisionCallback mCollisionCallback = new ICollisionCallback() {
		   @Override
		   public boolean onCollision(IShape pCheckShape, IShape pTargetShape) {
			   Sprite bunchie1 = (Sprite) pCheckShape;
			   Sprite bunchie2 = (Sprite) pTargetShape;
			   bunchie2.setColor(150,0,0);
			   
			   if (collided == false) {
				   velocityX = (bunchie1.getX() - previousX);
				   velocityY = (bunchie1.getY() - previousY);
			   }
			   //ALSO does other logic
			   collided = true;
			   return false;
		   }
	   };
	   
	   CollisionHandler myCollisionHandler = new CollisionHandler(mCollisionCallback, bunchieLlama, bunchieLlama2);
	   this.mEngine.registerUpdateHandler(myCollisionHandler);
	   
	   float numbersHeight = stripTiledTextureRegion.getHeight();
	   float numbersWidth = stripTiledTextureRegion.getWidth();
	   
	   mNumbers = new Numbers ((CAMERA_WIDTH - numbersWidth) / 2, CAMERA_HEIGHT - (numbersHeight + (numbersHeight / 4)), stripTiledTextureRegion, this.mEngine.getVertexBufferObjectManager());

	   mScene.attachChild(mNumbers);
	   mScene.attachChild(bunchieLlama2);
	   mScene.attachChild(bunchieLlama);
	   this.mScene.registerTouchArea(bunchieLlama);
	   pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

	// ===========================================================
	// Methods
	// ===========================================================
   

    @Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	    if (pSceneTouchEvent.isActionDown())
	    {
	    	bunchieLlama.setPosition(pSceneTouchEvent.getX() - bunchieLlama.getWidth() / 2, 
					   pSceneTouchEvent.getY() - bunchieLlama.getHeight() / 2);
	    		/*
	    	velocityX = (bunchieLlama.getX() - previousX) / 20;
	    	velocityY = (bunchieLlama.getY() - previousY) / 20;	*/		
	    }
	    return false;
	}

    //this will be our game loop!
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		/*bunchieLlama.setPosition(bunchieLlama.getX() - 5, bunchieLlama.getY() - 1);
		bunchieLlama.setWidth(bunchieLlama.getWidth() + 10);
		bunchieLlama.setHeight(bunchieLlama.getHeight() + 10);
		mNumbers.setCurrentTileIndex(index);

		index++;
		
		if (index > 9)
			index = 0;
		*/
		bunchieLlama2.setX(bunchieLlama2.getX() + velocityX);
		bunchieLlama2.setY(bunchieLlama2.getY() + velocityY);	
		
		
		mNumbers.setCurrentTileIndex(index);
		
		if (bunchieLlama2.getX() < -125 || bunchieLlama2.getY() < -203) {
			if (leftScreen == false) {
				mScene.detachChild(bunchieLlama2);
				leftScreen = true;
			}
		}
		
		else if (bunchieLlama2.getX() > 905 || bunchieLlama2.getY() > 683) {
			if (leftScreen == false) {
				mScene.detachChild(bunchieLlama2);
				leftScreen = true;
			}
		}
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public class Numbers extends AnimatedSprite {
		public Numbers (final float pX, final float pY,
				final ITiledTextureRegion pTiledTextureRegion,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		}
	}
}
