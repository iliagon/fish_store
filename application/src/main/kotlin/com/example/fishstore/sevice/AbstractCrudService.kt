//package com.example.fishstore.sevice
//
//import com.example.fishstore.config.NotFoundException
//import com.example.fishstore.models.entity.BaseEntity
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.data.jpa.repository.JpaRepository
//import com.example.fishstore.mapper.PayloadEntityMapper
//import java.util.stream.Collectors
//import org.springframework.dao.EmptyResultDataAccessException
//import org.springframework.transaction.annotation.Transactional
//
///**
// * Implement default service operations for a specific payload/entity type.
// *
// * @param <T>  the DTO type
// * @param <E>  the Entity type the repository manages
// * @param <ID> the type of the Id of the entity
// */
//@Transactional
//abstract class AbstractCrudService<T, E : BaseEntity<ID>, ID : Any> {
//    @Autowired
//    protected lateinit var repository: JpaRepository<E, ID>
//
//    @Autowired
//    protected lateinit var mapper: PayloadEntityMapper<T, E>
//
//    fun findAll(): List<T> {
//        return findAllEntity().stream()
//            .map { mapper.toDto(it) }
//            .collect(Collectors.toList())
//    }
//
//    fun findAllEntity(): List<E> {
//        return repository.findAll()
//    }
//
//    fun findById(id: ID): T {
//        return mapper.toDto(findEntityById(id))
//    }
//
//    fun findEntityById(id: ID): E {
//        return repository.findById(id)
//            .orElseThrow { NotFoundException(notFoundMessage(id)) }
//    }
//
//    fun create(payload: T): T {
//        val entity = mapper.toEntity(payload)
//        val savedEntity = create(entity)
//        return mapper.toDto(savedEntity)
//    }
//
//    /**
//     * just to overriding in child classes
//     */
//    protected fun create(entity: E): E {
//        return save(entity)
//    }
//
//    fun update(payload: T): T {
//        val entity = mapper.toEntity(payload)
//        checkIfExists(entity.id!!)
//        val savedEntity = update(entity)
//        return mapper.toDto(savedEntity)
//    }
//
//    /**
//     * just to overriding in child classes
//     */
//    protected fun update(entity: E): E {
//        return save(entity)
//    }
//
//    /**
//     * just to overriding in child classes
//     */
//    protected fun save(entity: E): E {
//        return repository.save(entity)
//    }
//
//    fun delete(id: ID) {
//        try {
//            repository.deleteById(id)
//        } catch (ex: EmptyResultDataAccessException) {
//            throw NotFoundException(devMessage = notFoundMessage(id), exception = ex)
//        }
//    }
//
//    fun checkIfExists(id: ID) {
//        repository.findById(id).orElseThrow { NotFoundException(devMessage = notFoundMessage(id)) }
//    }
//
//    protected abstract fun notFoundMessage(id: ID): String
//}