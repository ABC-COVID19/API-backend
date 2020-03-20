import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Abccovid19TestModule } from '../../../test.module';
import { RevisionUpdateComponent } from 'app/entities/revision/revision-update.component';
import { RevisionService } from 'app/entities/revision/revision.service';
import { Revision } from 'app/shared/model/revision.model';

describe('Component Tests', () => {
  describe('Revision Management Update Component', () => {
    let comp: RevisionUpdateComponent;
    let fixture: ComponentFixture<RevisionUpdateComponent>;
    let service: RevisionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Abccovid19TestModule],
        declarations: [RevisionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RevisionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RevisionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RevisionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Revision(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Revision();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
