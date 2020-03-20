package pt.tech4covid.web.rest;

import pt.tech4covid.domain.Topic;
import pt.tech4covid.repository.TopicRepository;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.tech4covid.domain.Topic}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TopicResource {

    private final Logger log = LoggerFactory.getLogger(TopicResource.class);

    private static final String ENTITY_NAME = "topic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicRepository topicRepository;

    public TopicResource(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * {@code POST  /topics} : Create a new topic.
     *
     * @param topic the topic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topic, or with status {@code 400 (Bad Request)} if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) throws URISyntaxException {
        log.debug("REST request to save Topic : {}", topic);
        if (topic.getId() != null) {
            throw new BadRequestAlertException("A new topic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Topic result = topicRepository.save(topic);
        return ResponseEntity.created(new URI("/api/topics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topics} : Updates an existing topic.
     *
     * @param topic the topic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topic,
     * or with status {@code 400 (Bad Request)} if the topic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topics")
    public ResponseEntity<Topic> updateTopic(@RequestBody Topic topic) throws URISyntaxException {
        log.debug("REST request to update Topic : {}", topic);
        if (topic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Topic result = topicRepository.save(topic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topic.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /topics} : get all the topics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topics in body.
     */
    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        log.debug("REST request to get all Topics");
        return topicRepository.findAll();
    }

    /**
     * {@code GET  /topics/:id} : get the "id" topic.
     *
     * @param id the id of the topic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topics/{id}")
    public ResponseEntity<Topic> getTopic(@PathVariable Long id) {
        log.debug("REST request to get Topic : {}", id);
        Optional<Topic> topic = topicRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(topic);
    }

    /**
     * {@code DELETE  /topics/:id} : delete the "id" topic.
     *
     * @param id the id of the topic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        log.debug("REST request to delete Topic : {}", id);
        topicRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
