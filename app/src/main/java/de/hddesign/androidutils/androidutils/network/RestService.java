package de.hddesign.androidutils.androidutils.network;

import java.util.List;

import de.hddesign.androidutils.androidutils.network.dto.PhotoV1Dto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestService {

    @GET("/photos")
    Call<List<PhotoV1Dto>> getPhotos();

}