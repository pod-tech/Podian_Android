package com.seawindsolution.podphotographer.Servicew;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetailSerializer {
    public class Geometry {
        public class Location {
            Double lat;
            Double lng;
        }
        Location location;
    }

    public class AddressComponent {
        String long_name;
        String short_name;
        String[] types;
    }

    public class Photo {
        int height;
        int width;
        String photo_reference;
    }

    public class Place {
        @SerializedName("place_id")
        public String place_id;

        @SerializedName("name")
        public String name;

        @SerializedName("address_components")
        public List<AddressComponent> address_components;

        @SerializedName("formatted_address")
        public String formatted_address;

        @SerializedName("international_phone_number")
        public String phone_number;

        @SerializedName("photos")
        public List<Photo> photos;

        @SerializedName("geometry")
        public Geometry geometry;

        @SerializedName("icon")
        public String icon;

        @SerializedName("rating")
        Double rating;

        @SerializedName("types")
        public String[] types;

        @SerializedName("url")
        public String url;

        @SerializedName("vicinity")
        public String vicinity;

        @SerializedName("website")
        public String website;

        public String getID() {
            return place_id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return formatted_address;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public String getPhotoURL(int maxwidth) {
            if (!photos.isEmpty()) {
                String key = PlaceAutocompleteAPI.KEY;
                Photo photo = photos.get(0);
                String ref = photo.photo_reference;
                String url = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%d&photoreference=%s&key=%s", maxwidth, ref, key);
                return url;
            }
            return null;
        }

        @Override
        public String toString() {
            return "Place{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @SerializedName("result")
    public Place result;

    @SerializedName("status")
    public String status;

    public Place getPlace() {
        return result;
    }
}