

package com.example.antitextbook;

        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.ImageView;

        import com.google.android.gms.tasks.Continuation;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.FirebaseApp;
        import com.google.firebase.storage.FileDownloadTask;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnPausedListener;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageException;
        import com.google.firebase.storage.StorageMetadata;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;

public class StorageVoids extends AppCompatActivity {
    // [START storage_field_declaration]
    // [END storage_field_declaration]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_storage);

        // [START storage_field_initialization]
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // [END storage_field_initialization]

        includesForCreateReference();
    }

    public void includesForCreateReference() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // ## Создать ссылку

        // [START create_storage_reference]
        // Создаем ссылку на хранилище из нашего приложения
        StorageReference storageRef = storage.getReference();
        // [END create_storage_reference]

        // [START create_child_reference]
        // Создание дочерней ссылки
        // imagesRef теперь указывает на "изображения"
        StorageReference imagesRef = storageRef.child("images");

        // Ссылки на ребенка также могут принимать пути
        // spaceRef теперь указывает на "images / space.jpg
        // imagesRef по-прежнему указывает на "изображения"
        StorageReference spaceRef = storageRef.child("images/space.jpg");
        // [END create_child_reference]

        // ## Перемещение по ссылкам

        // [START navigate_references]
        // getParent позволяет нам перемещать нашу ссылку на родительский узел
        // imagesRef теперь указывает на 'images'
        imagesRef = spaceRef.getParent();

        /// getRoot позволяет нам полностью перемещаться в начало нашего ведра
        // rootRef теперь указывает на корень
        StorageReference rootRef = spaceRef.getRoot();
        // [END navigate_references]

        // [START chain_navigation]
        // Ссылки могут быть соединены вместе несколько раз
        // earthRef указывает на «images/earth.jpg»
        StorageReference earthRef = spaceRef.getParent().child("earth.jpg");

        // nullRef имеет значение null, так как родительский корень имеет значение null
        StorageReference nullRef = spaceRef.getRoot().getParent();
        // [END chain_navigation]

        // ## Справочные свойства

        // [START reference_properties]
        // Путь ссылки: "images / space.jpg"
        // Это аналогично пути файла на диске
        spaceRef.getPath();

        // Имя ссылки - это последний сегмент полного пути: "space.jpg"
        // Это аналогично имени файла
        spaceRef.getName();

        // Ведение каталога - это имя хранилища, в котором хранятся файлы
        spaceRef.getBucket();
        // [END reference_properties]

        // ## Полный пример

        // [START reference_full_example]
        // Точки к корневой ссылке
        storageRef = storage.getReference();

        // Точки к «изображениям»
        imagesRef = storageRef.child("images");

        // Точки к "images/space.jpg"
        // Обратите внимание, что вы можете использовать переменные для создания дочерних значений
        String fileName = "space.jpg";
        spaceRef = imagesRef.child(fileName);

        // Путь к файлу - "images / space.jpg"
        String path = spaceRef.getPath();

        // Имя файла: "space.jpg"
        String name = spaceRef.getName();

        // Точки к «изображениям»
        imagesRef = spaceRef.getParent();
        // [END reference_full_example]
    }

    public void includesForUploadFiles() throws FileNotFoundException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Создаем ссылку на хранилище из нашего приложения
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку на "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        // Создаем ссылку на 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

        // Хотя имена файлов одинаковы, ссылки указывают на разные файлы
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
        // [END upload_create_reference]


        ImageView imageView = (ImageView)findViewById(android.R.id.text1);

        // [START upload_memory]
        // Получаем данные из ImageView в виде байтов
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка неудачных загрузок...
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() содержит метаданные файлов, такие как размер, тип содержимого и т. д.
                // ...
            }
        });
        // [END upload_memory]

        // [START upload_stream]
        InputStream stream = new FileInputStream(new File("path/to/images/rivers.jpg"));

        uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка неудачных загрузок...
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata () содержит метаданные файлов, такие как размер, тип содержимого и т. д.
                // ...
            }
        });
        // [END upload_stream]

        // [START upload_file]
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        // Зарегистрируйте наблюдателей для прослушивания, когда загрузка выполнена или если она не работает
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка неудачных загрузок
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata () содержит метаданные файлов, такие как размер, тип содержимого и т. д.
                // ...
            }
        });
        // [END upload_file]

        // [START upload_with_metadata]
        // Создание метаданных файлов, включая тип содержимого
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        // Загрузите файл и метаданные
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file, metadata);
        // [END upload_with_metadata]

        // [START manage_uploads]
        uploadTask = storageRef.child("images/mountains.jpg").putFile(file);

        // Приостановить загрузку
        uploadTask.pause();

        // Возобновление загрузки
        uploadTask.resume();

        // Отмена загрузки
        uploadTask.cancel();
        // [END manage_uploads]

        // [START monitor_upload_progress]
        // Наблюдайте за событиями изменения состояния, такими как прогресс, пауза и возобновление
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });
        // [END monitor_upload_progress]

        // [START upload_complete_example]
        // Файл или Blob
        file = Uri.fromFile(new File("path/to/mountains.jpg"));

        // Создание метаданных файла
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        // Загружаем файл и метаданные на путь «images/mountains.jpg»
        uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

        // Прослушивание изменений состояния, ошибок и завершения загрузки.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обработка неудачных загрузок
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Обработка успешных загрузок при завершении
                // ...
            }
        });
        // [END upload_complete_example]

        // [START upload_get_download_url]
        final StorageReference ref = storageRef.child("images/mountains.jpg");
        uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Продолжаем задачу по загрузке URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Обработка сбоев
                    // ...
                }
            }
        });
        // [END upload_get_download_url]
    }

    public void includesForDownloadFiles() throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START download_create_reference]
        // Создаем ссылку на хранилище из нашего приложения
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку с начальным пути и именем файла
        StorageReference pathReference = storageRef.child("images/stars.jpg");

        // Создаем ссылку на файл из URI облачного хранилища Google
        StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

        // Создаем ссылку с URL-адреса HTTPS
        // Обратите внимание, что в URL-адресе символы URL-адреса экранированы!
        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
        // [END download_create_reference]

        // [START download_to_memory]
        StorageReference islandRef = storageRef.child("images/island.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Данные для «images/island.jpg» - это возврат, используйте это при необходимости
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обрабатывать любые ошибки
            }
        });
        // [END download_to_memory]

        // [START download_to_local_file]
        islandRef = storageRef.child("images/island.jpg");

        File localFile = File.createTempFile("images", "jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Создан локальный файл temp
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обрабатывать любые ошибки
            }
        });
        // [END download_to_local_file]

        // [START download_via_url]
        storageRef.child("users/me/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Получил URL для загрузки 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обрабатывать любые ошибки
            }
        });
        // [END download_via_url]

        // [START download_full_example]
        storageRef.child("users/me/profile.png").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Используйте байты для отображения изображения
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Обрабатывать любые ошибки
            }
        });
        // [END download_full_example]
    }

    public void includesForFileMetadata() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START metadata_get_storage_reference]
        // Создаем ссылку на хранилище из нашего приложения
        StorageReference storageRef = storage.getReference();

        // Получить ссылку на файл
        StorageReference forestRef = storageRef.child("images/forest.jpg");
        // [END metadata_get_storage_reference]


        // [START get_file_metadata]
        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Метаданные теперь содержат метаданные для 'images/forest.jpg'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, произошла ошибка!
            }
        });
        // [END get_file_metadata]

        // [START update_file_metadata]
        // Создание метаданных файлов, включая тип содержимого
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("myCustomProperty", "myValue")
                .build();

        // Обновление свойств метаданных
        forestRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Обновлены метаданные в памятиMetadata
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, произошла ошибка!
                    }
                });
        // [END update_file_metadata]
    }

    public void includesForMetadata_delete() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference forestRef = storageRef.child("images/forest.jpg");

        // [START delete_file_metadata]
        // Создание метаданных файла с указанием свойства для удаления
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(null)
                .build();

        // Удалить свойство метаданных
        forestRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // metadata.contentType должен иметь значение null
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, произошла ошибка!
                    }
                });
        // [END delete_file_metadata]
    }

    public void includesForMetadata_custom() {
        // [START custom_metadata]
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("location", "Yosemite, CA, USA")
                .setCustomMetadata("activity", "Hiking")
                .build();
        // [END custom_metadata]
    }

    public void includesForDeleteFiles() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START delete_file]
        // Создаем ссылку на хранилище из нашего приложения
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку на файл для удаления
        StorageReference desertRef = storageRef.child("images/desert.jpg");

        // Удалить файл
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, произошла ошибка!
            }
        });
        // [END delete_file]
    }

    public void nonDefaultBucket() {
        // [START storage_non_default_bucket]
        // Получаем ведро для хранения по умолчанию
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
        // [END storage_non_default_bucket]
    }

    public void customApp() {
        FirebaseApp customApp = FirebaseApp.initializeApp(this);

        // [START storage_custom_app]
        // Получить ведро по умолчанию из пользовательского FirebaseApp
        FirebaseStorage storage = FirebaseStorage.getInstance(customApp);

        // Получаем ведро не по умолчанию из пользовательского FirebaseApp
        FirebaseStorage customStorage = FirebaseStorage.getInstance(customApp, "gs://my-custom-bucket");
        // [END storage_custom_app]
    }

    // [START storage_custom_failure_listener]
    class MyFailureListener implements OnFailureListener {
        @Override
        public void onFailure(@NonNull Exception exception) {
            int errorCode = ((StorageException) exception).getErrorCode();
            String errorMessage = exception.getMessage();
            // проверяем errorCode и errorMessage и обрабатываем соответственно
        }
    }
    // [END storage_custom_failure_listener]

}