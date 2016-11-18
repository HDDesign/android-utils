package de.hddesign.androidutils.androidutils.network.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import de.hddesign.androidutils.androidutils.network.utils.FluentBuilder;
import de.hddesign.androidutils.androidutils.network.utils.MoreObjects;
import de.hddesign.androidutils.androidutils.network.utils.Objects;


@JsonDeserialize(builder = PhotoV1Dto.Builder.class)
public class PhotoV1Dto implements Dto {
    private final Long id;
    private final Long albumId;
    private final String title;
    private final String url;
    private final String thumbnailUrl;

    private PhotoV1Dto(Builder builder) {
        this.id = builder.id;
        this.albumId = builder.albumId;
        this.title = builder.title;
        this.url = builder.url;
        this.thumbnailUrl = builder.thumbnailUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, albumId, title, url, thumbnailUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PhotoV1Dto other = (PhotoV1Dto) obj;
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.albumId, other.albumId)
                && Objects.equal(this.title, other.title)
                && Objects.equal(this.url, other.url)
                && Objects.equal(this.thumbnailUrl, other.thumbnailUrl);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("albumId", albumId)
                .add("title", title)
                .add("url", url)
                .add("thumbnailUrl", thumbnailUrl)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder implements FluentBuilder<PhotoV1Dto> {
        private Long id;
        private Long albumId;
        private String title;
        private String url;
        private String thumbnailUrl;

        public Builder withId(Long id) {
            this.id = id;

            return this;
        }

        public Builder withAlbumId(Long albumId) {
            this.albumId = albumId;

            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;

            return this;
        }

        public Builder withThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;

            return this;
        }

        @Override
        public PhotoV1Dto build() {
            return new PhotoV1Dto(this);
        }
    }
}
