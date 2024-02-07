package com.example.actividad3_m08;


    import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

        private List<String> MP;
        private OnItemClickListener listener;

        public SongAdapter(List<String> songList) {
            this.MP = songList;
        }

        public interface OnItemClickListener { //INDENTIFICA LA POSICOION EN LA LISTA
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) { //SI SE DIO CLICK O NO
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String songName = MP.get(position);
            holder.textSongName.setText(songName);
        }

        @Override
        public int getItemCount() {
            return MP.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textSongName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textSongName = itemView.findViewById(R.id.textSongName);

                // Agregar un clic al elemento del RecyclerView
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position);


                            }
                        }
                    }
                });
            }
        }
    }


