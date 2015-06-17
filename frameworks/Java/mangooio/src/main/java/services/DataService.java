package services;

import interfaces.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import models.Fortune;
import models.World;

import com.google.inject.Inject;

import de.svenkubiak.mangoo.mongodb.MongoDB;

public class DataService {
    private MongoDB mongoDB;

    @Inject
    public DataService(MongoDB mongoDB) {
    	this.mongoDB = mongoDB;
        this.mongoDB.ensureIndexes(true);
    }

    public World findById(int id) {
        return this.mongoDB.getDatastore().find(World.class).field("id").equal(id).retrievedFields(false, "_id").get();
    }
    
    public List<World> find(int queries) {
    	return this.mongoDB.getDatastore().find(World.class).retrievedFields(false, "_id").asList();
    }

	public void save(Object object) {
		this.mongoDB.getDatastore().save(object);
	}
	
	public List<World> getWorlds(int queries) {
		if (queries <= 1) {
			queries = 1;
		} else if (queries > 500) {
			queries = 500;
		}
		
		List<World> worlds = new ArrayList<World>();
		for (int i=0; i < queries; i++) {
			int id = ThreadLocalRandom.current().nextInt(Constants.ROWS) + 1;
			worlds.add(findById(id));
		}
		return worlds;
	}

	public List<Fortune> findAllFortunes() {
		return this.mongoDB.getDatastore().find(Fortune.class).order("message").asList();
	}
}