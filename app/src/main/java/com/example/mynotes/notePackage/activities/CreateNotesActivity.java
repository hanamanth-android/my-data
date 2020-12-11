package com.example.mynotes.notePackage.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mynotes.R;
import com.example.mynotes.notePackage.database.NoteViewmodel;
import com.example.mynotes.notePackage.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

public class CreateNotesActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private static Note note;
    private View viewSubtitleIndicator;
    private ImageView imageNote;
    private TextView textWebUrl;
    private LinearLayout layoutWebUrl;

    private String selectNoteColor;
    private String selectImagePath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private AlertDialog dialogAddUrl, dialogDeleteNote;
    private NoteViewmodel viewmodel;


    private Note alreadyAvailableNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        TextView imageBack = findViewById(R.id.imageback);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputnoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputnotesubtitle);
        inputNoteText = findViewById(R.id.inputnote);
        textDateTime = findViewById(R.id.textdatetime);
        viewSubtitleIndicator = findViewById(R.id.viewsubtitleindicator);
        imageNote = findViewById(R.id.imageNote);
        textWebUrl = findViewById(R.id.textWebUrl);
        layoutWebUrl = findViewById(R.id.layoutWebUrl);
        viewmodel = new ViewModelProvider(this).get(NoteViewmodel.class);

        textDateTime.setText(
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                        .format(new Date())
        );
        ImageView imageSave = findViewById(R.id.imagesave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveNote();
                Intent intent = new Intent();
                if (saveNote()) {
                    intent.putExtra("extra_reply", note);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        selectNoteColor = "#333333";
        selectImagePath = "";
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setVieworUpdate();
        }

        findViewById(R.id.imageRemovalwebUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textWebUrl.setText("");
                layoutWebUrl.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.imageRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
                selectImagePath = "";
            }
        });


        initMiscellaneous();
        setSubtitleIndicatorColor();

    }

    private void setVieworUpdate() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubTitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(textDateTime.getText().toString());
        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
            selectImagePath = alreadyAvailableNote.getImagePath();
        }
        if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
            textWebUrl.setText(alreadyAvailableNote.getWebLink());
            layoutWebUrl.setVisibility(View.VISIBLE);
        }
    }

    private boolean saveNote() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "note title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;

        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty() &&
                inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "note can not be empty", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            note = new Note();
            note.setTitle(inputNoteTitle.getText().toString());
            note.setSubTitle(inputNoteSubtitle.getText().toString());
            note.setNoteText(inputNoteText.getText().toString());
            note.setDateTime(textDateTime.getText().toString());
            note.setColor(selectNoteColor);
            note.setImagePath(selectImagePath);
            if (layoutWebUrl.getVisibility() == View.VISIBLE) {
                note.setWebLink(textWebUrl.getText().toString());
            }
            if (alreadyAvailableNote != null) {
                note.setId(alreadyAvailableNote.getId());
            }
            return true;
        }
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
        final ImageView imagecolor1 = layoutMiscellaneous.findViewById(R.id.imagecolor1);
        final ImageView imagecolor2 = layoutMiscellaneous.findViewById(R.id.imagecolor2);
        final ImageView imagecolor3 = layoutMiscellaneous.findViewById(R.id.imagecolor3);
        final ImageView imagecolor4 = layoutMiscellaneous.findViewById(R.id.imagecolor4);
        final ImageView imagecolor5 = layoutMiscellaneous.findViewById(R.id.imagecolor5);

        layoutMiscellaneous.findViewById(R.id.viewcolor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoteColor = "#333333";
                imagecolor1.setImageResource(R.drawable.ic_done);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewcolor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoteColor = "#FDBE3B";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(R.drawable.ic_done);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewcolor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoteColor = "#FF4842";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(R.drawable.ic_done);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewcolor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoteColor = "#3A52FC";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(R.drawable.ic_done);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewcolor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNoteColor = "#000000";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicatorColor();
            }
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "#FDBE3B":
                    layoutMiscellaneous.findViewById(R.id.viewcolor2).performClick();
                    break;
                case "#FF4842":
                    layoutMiscellaneous.findViewById(R.id.viewcolor3).performClick();
                    break;
                case "#3A52FC":
                    layoutMiscellaneous.findViewById(R.id.viewcolor4).performClick();
                    break;
                case "#000000":
                    layoutMiscellaneous.findViewById(R.id.viewcolor5).performClick();
                    break;
            }
        }
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNotesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);

                } else {
                    selectImage();
                }
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddUrldialog();
            }
        });
        if (alreadyAvailableNote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutdeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutdeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();

                }
            });
        }
    }

    private void showDeleteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer));
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable());
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent=new Intent();
                    intent.putExtra("isNoteDeleted",true);
                    setResult(RESULT_OK,intent);
                    finish();*/
                    viewmodel.delete(alreadyAvailableNote);
                    Toast.makeText(CreateNotesActivity.this, "note deleted", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CreateNotesActivity.this,NoteHomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectImageUri = data.getData();
                if (selectImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        selectImagePath = getPathFromUri(selectImageUri);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri conUri) {
        String filepath;
        Cursor cursor = getContentResolver()
                .query(conUri, null, null, null, null);
        if (cursor == null) {
            filepath = conUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filepath = cursor.getString(index);
            cursor.close();
        }
        return filepath;
    }

    private void showAddUrldialog() {
        if (dialogAddUrl == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);
            dialogAddUrl = builder.create();
            if (dialogAddUrl.getWindow() != null) {
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputUrl = view.findViewById(R.id.inputURL);
            inputUrl.requestFocus();
            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputUrl.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateNotesActivity.this, "enter url", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString()).matches()) {
                        Toast.makeText(CreateNotesActivity.this, "enter valid url", Toast.LENGTH_SHORT).show();
                    } else {
                        textWebUrl.setText(inputUrl.getText().toString());
                        layoutWebUrl.setVisibility(View.VISIBLE);
                        dialogAddUrl.dismiss();
                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddUrl.dismiss();
                }
            });
        }
        dialogAddUrl.show();
    }
}