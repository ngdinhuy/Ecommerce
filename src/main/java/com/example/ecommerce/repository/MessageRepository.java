package com.example.ecommerce.repository;

import com.example.ecommerce.model.MessageModel;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
    @Query(value = "select m.*\n" +
            "from tbl_message m\n" +
            "where m.id in (select max(m.id) as max_id\n" +
            "               from tbl_message m\n" +
            "               where m.id_sender = :user_id or m.id_receiver = :user_id\n" +
            "               group by least(m.id_sender, m.id_receiver), greatest(m.id_sender, m.id_receiver)\n" +
            "              );", nativeQuery = true)
    List<MessageModel> findListLateMessage(@Param("user_id") Integer idUser);

    List<MessageModel> findMessageModelsBySenderAndRecieverAndReadStatus(User sender, User receiver, Integer readStatus);

    @Query(
            value = "select * " +
                    "from tbl_message p\n" +
                    " where (p.id_sender = :id_sender and p.id_receiver = :id_receiver) or (p.id_sender = :id_receiver and p.id_receiver = :id_sender)\n" +
                    " order by p.id desc", nativeQuery = true)
    Page<MessageModel> getMessageModelBySenderAndReceiver(final Pageable pageable, @Param("id_sender") Integer idSender, @Param("id_receiver") Integer idReceiver);

    @Query(
            value = "select * " +
                    "from tbl_message p\n" +
                    " where (p.id_sender = :id_sender and p.id_receiver = :id_receiver) or (p.id_sender = :id_receiver and p.id_receiver = :id_sender)\n" +
                    " order by p.id desc", nativeQuery = true)
    List<MessageModel> getMessageModelBySenderAndReceiver(@Param("id_sender") Integer idSender, @Param("id_receiver") Integer idReceiver);

    @Modifying
    @Query( value = "update tbl_message m\n" +
            "set m.read_status = 1\n" +
            " where m.id_sender = :id_sender", nativeQuery = true)
    void updateMessageRead(@Param("id_sender") Integer idSender);

}
