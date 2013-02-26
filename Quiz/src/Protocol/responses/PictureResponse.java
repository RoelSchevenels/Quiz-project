package Protocol.responses;

import java.io.IOException;

import BussinesLayer.resources.PictureResource;


public class PictureResponse extends Response{
	private static final long serialVersionUID = 8318083326471283040L;
	private byte[] image;
	
	
	public PictureResponse(int RequestId) {
		super(RequestId);
	}

	public void setPictureResource(PictureResource r) throws IOException {
		this.image = r.getAsByteArray();
	}
	
	public PictureResource getPictureResource() throws IOException {
		return new PictureResource(image);
	}

	
}
