package fr.istic.TP1;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Key;

public class Bulk {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key bulkKey;

    @Persistent
    private User bulkCreator;

    @Persistent
    public String bulkTitle;

    @Persistent
    private Date creationDate;
    
    @Persistent
    private float bulkPrice;

    public Bulk(User poAuthor, String psTitle, Date pdDate, float pfPrice) {
        this.bulkCreator = poAuthor;
        this.bulkTitle = psTitle;
        this.creationDate = pdDate;
        this.bulkPrice = pfPrice;
    }

	public Key getBulkKey() {
		return bulkKey;
	}

	public void setBulkKey(Key poBulkKey) {
		this.bulkKey = poBulkKey;
	}

	public User getBulkCreator() {
		return bulkCreator;
	}

	public void setBulkCreator(User poBulkCreator) {
		this.bulkCreator = poBulkCreator;
	}

	public String getBulkTitle() {
		return bulkTitle;
	}

	public void setBulkTitle(String psBulkTitle) {
		this.bulkTitle = psBulkTitle;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date pdCreationDate) {
		this.creationDate = pdCreationDate;
	}

	public float getBulkPrice() {
		return bulkPrice;
	}

	public void setBulkPrice(float pfBulkPrice) {
		this.bulkPrice = pfBulkPrice;
	}
    
}
