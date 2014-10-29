package ie.itcarlow.box2ddemo;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;


public class Box2DSpriteCollisions extends BaseGameActivity implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mTextureAustrianBear;
	private BitmapTextureAtlas mTexturePiglet;
	private ITextureRegion mAustrianBearTextureRegion;
	private ITextureRegion mPigletTextureRegion;
	private Scene mScene;
	private Sprite mPiglet;	
	private Sprite austrianBear;
	
	private PhysicsWorld mPhysicsWorld;
	private FixtureDef PLAYER_FIX;
	
	//used for moving the bear towards the piglet
	private Vector2 vel;
	private String str;
	
	boolean bearHitPig;
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
    	
    	 PLAYER_FIX = PhysicsFactory.createFixtureDef(1.5f,0.45f,0.3f);
    	 bearHitPig = false;

    	 loadGfx();
		 pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    private void loadGfx() {

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");  
        mTextureAustrianBear = new BitmapTextureAtlas(getTextureManager(), 46, 54);  
        mAustrianBearTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTextureAustrianBear, this, "austrian_bear.png", 0, 0);
        mTextureAustrianBear.load();
        
        mTexturePiglet = new BitmapTextureAtlas(getTextureManager(), 46, 54);
        mPigletTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexturePiglet, this, "piglet.png", 0, 0);
        mTexturePiglet.load();
    }

    @Override
  	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
  			throws Exception {
    	
    	
    	this.mScene = new Scene();
  		this.mScene.setBackground(new Background(0, 125, 58));
  		setUpBox2DWorld();
  		mPhysicsWorld.setContactListener(CreateContactListener());
  	    pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);  		
  	}


    @Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) 
          throws Exception {

    	
    	mEngine.registerUpdateHandler(this);
       // Setup coordinates for the sprite in order that it will
       //  be centered on the camera.
	   final float centerX = (CAMERA_WIDTH - this.mAustrianBearTextureRegion.getWidth()) / 2;
	   final float centerY = (CAMERA_HEIGHT - this.mAustrianBearTextureRegion.getHeight()) / 2;
 
	   // Create the Austrian bear and add it to the scene.
	   
	   austrianBear = new Sprite(centerX, centerY, this.mAustrianBearTextureRegion, this.getVertexBufferObjectManager())
	   {
           @Override
           public boolean onAreaTouched(final TouchEvent event,
                                        final float pTouchAreaLocalX,
                                        final float pTouchAreaLocalY) {
        	   if (event.getAction() == MotionEvent.ACTION_UP) {
        		   //setBodyPosition(this, event.getX() - this.getWidth() / 2, event.getY() - this.getHeight() / 2);
        		   int speed = 10;
        		   vel = new Vector2((mPiglet.getX() - austrianBear.getX()) / speed, (mPiglet.getY() - austrianBear.getY()) / speed); 
        		   Vector2 velocity = Vector2Pool.obtain(vel.x, vel.y);
        		   Body b = (Body) this.getUserData();
        		   b.applyLinearImpulse(velocity, b.getWorldCenter());
        		   Vector2Pool.recycle(velocity);        		   
        	   }
               return true;
           }
       };
	   
	   this.mScene.registerTouchArea(austrianBear);
	   
	   mPiglet = new Sprite(centerX - 100, centerY - 100,  mPigletTextureRegion, this.mEngine.getVertexBufferObjectManager());
	   createPhysicsBodies(mPiglet, "pig");
	   mScene.attachChild(mPiglet);	 
	   
	   // The bear sprite (unlike the piglet sprite) is a local variable, 
	   //  so it must be passed to method createPhysicsBodies
	   createPhysicsBodies(austrianBear, "bear");  
	   mScene.attachChild(austrianBear);
	   pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

	// ===========================================================
	// Methods
	// ===========================================================

    private void setUpBox2DWorld() {
    	// Set up your physics world here.
    	final Vector2 v = Vector2Pool.obtain(0,0);
    	mPhysicsWorld = new PhysicsWorld(v, false);
    	Vector2Pool.recycle(v);
    	this.mScene.registerUpdateHandler(mPhysicsWorld);  		
    }
    
    private void createPhysicsBodies(final Sprite sprite, String name) {
    	// Create your Box2D bodies here.
    	Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, sprite, BodyType.DynamicBody, PLAYER_FIX);
    	body.setLinearDamping(0.4f);
    	body.setUserData(name);
    	sprite.setUserData(body);
    	mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));    	
    }
    
    /*
     * Helper method that translates the associated physics body to the specified coordinates.
     * 
	 * @param pX The desired x coordinate for this sprite.
	 * @param pY The desired y coordinate for this sprite.
     */
    private void setBodyPosition(final Sprite sprite, final float pX, final float pY) {
    	
    	final Body body = (Body) sprite.getUserData();
        final float widthD2 = sprite.getWidth() / 2;
        final float heightD2 = sprite.getHeight() / 2;
        final float angle = body.getAngle(); // keeps the body angle       
        final Vector2 v2 = Vector2Pool.obtain((pX + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (pY + heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        body.setTransform(v2, angle);
        Vector2Pool.recycle(v2);
    }

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub	
	
		if (bearHitPig == true) 
		{
			//mEngine.runOnUpdateThread(new Runnable() {
				//@Override
				//public void run() {
				PhysicsConnector physicsConnector = 
				mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(mPiglet);
				mPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
						
				mPhysicsWorld.destroyBody(physicsConnector.getBody());
				//}				
			//});
			bearHitPig = false;
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private ContactListener CreateContactListener() {
		ContactListener myContactListener = new ContactListener() {
			public void beginContact(Contact contact) {
				String a = (String) contact.getFixtureA().getBody().getUserData();
				String b = (String) contact.getFixtureB().getBody().getUserData();
				
				if (a != null && b != null) {
					if (a.equals("bear") && b.equals("pig") || a.equals("pig") && b.equals("bear"))
					{
						bearHitPig = true;
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		};
		return myContactListener;
	}
    

	    
    // ===========================================================
 	// Inner and Anonymous Classes
 	// ===========================================================
    
}
