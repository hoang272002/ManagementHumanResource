package model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftResponse {
    private int done;
    private String msg;
    private String microtime;
    private Data data;

    // Getters and setters
    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMicrotime() {
        return microtime;
    }

    public void setMicrotime(String microtime) {
        this.microtime = microtime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Nested Data class
    public static class Data {
        private List<Item> items;
        private int totalPage;

        // Getters and setters
        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        // Nested Item class
        public static class Item {
            private String id;
            private String brand_id;
            private int cat_id;
            private String name;
            private String images_src;
            private Brand brand;
            private List<Detail> detail;

            // Getters and setters
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBrandId() {
                return brand_id;
            }

            public void setBrandId(String brand_id) {
                this.brand_id = brand_id;
            }

            public int getCatId() {
                return cat_id;
            }

            public void setCatId(int cat_id) {
                this.cat_id = cat_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImagesSrc() {
                return images_src;
            }

            public void setImagesSrc(String images_src) {
                this.images_src = images_src;
            }

            public Brand getBrand() {
                return brand;
            }

            public void setBrand(Brand brand) {
                this.brand = brand;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            // Nested Brand class
            public static class Brand {
                private String id;
                private String title;

                // Getters and setters
                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

            // Nested Detail class
            public static class Detail {
                private String id;
                private String title;
                private String price;

                // Getters and setters
                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }
        }
    }
}