package com.mygdx.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapstate;
	float birdY = 0;
	float velocity = 0;
	int gamestate;
	float gravity = 2;
	Texture toptube;
	Texture bottomtube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerate;
	float tubeVelocity = 4;
	float[] tubeX = new float[4];
	float[] tubeOffset = new float[4];
	//int noOftubes = 4;
	float distanceTubes;
	Circle birdCircle;
	//ShapeRenderer shaper;
	Rectangle[] toptubeRectangle;
	Rectangle[] bottomtubeRectangle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	Texture gameOver;
	BitmapFont scoreFont;
	BitmapFont highScore;
	int highScoreRead = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		gameOver = new Texture("flappybirdover.jpg");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().scale(10);

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().scale(5);

		highScore = new BitmapFont();
		highScore.setColor(Color.WHITE);
		highScore.getData().scale(5);

		maxTubeOffset = Gdx.graphics.getHeight() - gap/2 - 100;

		randomGenerate = new Random();

		distanceTubes = Gdx.graphics.getWidth()/2;

		birdCircle = new Circle();
		//shaper = new ShapeRenderer();
		toptubeRectangle = new Rectangle[4];
		bottomtubeRectangle = new Rectangle[4];

		startGame();

	}
	public void startGame(){

		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for(int i=0 ; i<4; i++){
			tubeOffset[i] = (randomGenerate.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + i*distanceTubes + Gdx.graphics.getWidth();
			toptubeRectangle[i] = new Rectangle();
			bottomtubeRectangle[i] = new Rectangle();
		}
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (flapstate == 1) {
			flapstate = 0;
		} else {
			flapstate = 1;
		}

		if (gamestate == 1) {

			if(Gdx.input.justTouched()){
				velocity = -30;

			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				if(scoringTube < 3){
					scoringTube++;
				}
				else{
					scoringTube = 0;
				}
			}
			}

			for(int i=0 ; i<4; i++) {

				if(tubeX[i] < - toptube.getWidth()){

					tubeX[i] += 4 * distanceTubes;
					tubeOffset[i] = (randomGenerate.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}
				else{

					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				tubeX[i] = tubeX[i] - tubeVelocity;

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + (gap / 2) + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2) - bottomtube.getHeight() + tubeOffset[i]);

				toptubeRectangle[i] = new Rectangle(tubeX[i] , Gdx.graphics.getHeight() / 2 + (gap / 2) + tubeOffset[i] , toptube.getWidth() , toptube.getHeight());
				bottomtubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2) - bottomtube.getHeight() + tubeOffset[i] , bottomtube.getWidth() , bottomtube.getHeight());
			}
            if(birdY >= 0){
                velocity = velocity + gravity;
                birdY -= velocity;
            }

            if(birdY >= Gdx.graphics.getHeight() - birds[flapstate].getHeight())
			{
				birdY = Gdx.graphics.getHeight() - birds[flapstate].getHeight();
				velocity = 0;
			}

		}
		else if(gamestate == 0){

			if(Gdx.input.justTouched()){
				gamestate = 1;
			}
		}
		else if(gamestate == 2){
			batch.draw(gameOver , Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2 , Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
			if(highScoreRead < score){
				highScoreRead = score;
			}
			scoreFont.draw(batch ,"Score :" + String.valueOf(score) , Gdx.graphics.getWidth()/2 - 120 , Gdx.graphics.getHeight()/2 - 100);
			highScore.draw(batch , "High Score:" + String.valueOf(highScoreRead) , Gdx.graphics.getWidth()/2 -180 , Gdx.graphics.getHeight()/2 - 200);
			if(Gdx.input.justTouched()){
				gamestate = 1;
				startGame();
				scoringTube = 0;
				score = 0;
				velocity = 0;
			}
		}


        batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);
		font.draw(batch , String.valueOf(score) , 100 , 200);

        batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2 , birdY + birds[flapstate].getHeight()/2 , birds[flapstate].getHeight()/2);

		for (int i=0 ; i<4 ; i++){

			if(Intersector.overlaps(birdCircle , toptubeRectangle[i]) || Intersector.overlaps(birdCircle , bottomtubeRectangle[i])){

				gamestate = 2;

			}

		}
		//shaper.begin(ShapeRenderer.ShapeType.Filled);
        //shaper.setColor(Color.RED);
		//shaper.circle(birdCircle.x , birdCircle.y , birdCircle.radius);
		//shaper.end();
    }
}
