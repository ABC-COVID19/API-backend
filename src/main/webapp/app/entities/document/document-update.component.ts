import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDocument, Document } from 'app/shared/model/document.model';
import { DocumentService } from './document.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITopic } from 'app/shared/model/topic.model';
import { TopicService } from 'app/entities/topic/topic.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html'
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  topics: ITopic[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    date: [],
    abstractDesc: [],
    keywords: [],
    source: [],
    link: [],
    topic: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected documentService: DocumentService,
    protected topicService: TopicService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.updateForm(document);

      this.topicService.query().subscribe((res: HttpResponse<ITopic[]>) => (this.topics = res.body || []));
    });
  }

  updateForm(document: IDocument): void {
    this.editForm.patchValue({
      id: document.id,
      title: document.title,
      date: document.date,
      abstractDesc: document.abstractDesc,
      keywords: document.keywords,
      source: document.source,
      link: document.link,
      topic: document.topic
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('abccovid19App.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.createFromForm();
    if (document.id !== undefined) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  private createFromForm(): IDocument {
    return {
      ...new Document(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      date: this.editForm.get(['date'])!.value,
      abstractDesc: this.editForm.get(['abstractDesc'])!.value,
      keywords: this.editForm.get(['keywords'])!.value,
      source: this.editForm.get(['source'])!.value,
      link: this.editForm.get(['link'])!.value,
      topic: this.editForm.get(['topic'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ITopic): any {
    return item.id;
  }
}
